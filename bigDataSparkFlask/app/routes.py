import datetime

from flask import Blueprint, g, jsonify, request
from service import SparkService as sparkService, SparkService, SqlService

# 创建蓝图
main = Blueprint('api', __name__)


@main.route('/hello', methods=['post'])
def hello():
    return {'message': 'Hello, 1!'}


@main.route('/')
def home():
    return {'message': 'Hello, Home!'}


@main.route('/add')
def add():
    # 获取调度器实例
    scheduler = getattr(g, 'scheduler', None)

    if scheduler:
        # 添加一个新的任务（例如每 5 秒执行一次 another_task_function）
        job = scheduler.add_job(sparkService.test, 'interval', args=["1"], seconds=5)
        return jsonify({
            'message': 'New task added!',
            'task_id': job.id,
            'task_name': job.name
        })
    else:
        return jsonify({'message': 'Scheduler not found!'}), 500


@main.route('/list', methods=['post'])
def list_tasks():
    scheduler = getattr(g, 'scheduler', None)
    if scheduler:
        tasks = scheduler.get_jobs()
        return jsonify({'tasks': [{'id': task.id, 'name': task.name} for task in tasks]})
    else:
        return jsonify({'message': 'Scheduler not found!'}), 500


@main.route('/delete')
def delete_task():
    # 获取任务ID
    task_id = request.args.get('id')

    # 获取调度器
    scheduler = getattr(g, 'scheduler', None)

    # 如果没有调度器，直接返回错误信息
    if not scheduler:
        return jsonify({"message": -1}), 500

    # 尝试从调度器中获取任务
    job = scheduler.get_job(task_id)

    # 如果任务不存在，返回500错误
    if not job:
        return jsonify({"message": 0}), 500

    # 删除任务并返回成功信息
    scheduler.remove_job(task_id)
    return jsonify({"message": 1}), 200


@main.route('/test')
def test():
    scheduler = getattr(g, 'scheduler', None)
    if not scheduler:
        return jsonify({"message": -1}), 500
    scheduler.add_job(sparkService.test, 'interval', args=["1"], seconds=5)


@main.route('/refresh_index_daily')
def refresh_index_daily():
    scheduler = getattr(g, 'scheduler', None)
    if not scheduler:
        return jsonify({"message": -1}), 500
    scheduler.add_job(SqlService.index_daily, 'date', run_date=datetime.datetime.now(),
                      id="refresh_index_daily", name="refresh_index_daily")
    # 立即返回响应
    return jsonify({"message": "loading"}), 202  # 202表示请求已接受但未处理完毕


@main.route('/refresh_stock_data', methods=['post'])
def refresh_stock_data():
    scheduler = getattr(g, 'scheduler', None)
    if not scheduler:
        return jsonify({"message": -1}), 500
    # 获取当前时间
    now = datetime.datetime.now()

    # 添加一个周期性任务，每24小时执行一次，并立即执行一次
    def __refresh_stock_data__():
        print("refresh_stock_data")
        SparkService.merge_data()
        print("clean_stock_daily_data")
        SparkService.clean_stock_daily_data()
        SparkService.update_stock_data()
        SparkService.high_heat_count()
        SparkService.vol_quantiy_ratio()
        SparkService.spark_avg_increase()
        SparkService.spark_mutifactor_analysis()
        print("stock_info")
        SqlService.stock_info()
        scheduler.remove_job("refresh_stock_data")

    scheduler.add_job(
        __refresh_stock_data__,  # 任务函数
        'interval',  # 使用 interval 触发器，指定周期执行
        hours=24,  # 设置任务每24小时执行一次
        id="refresh_stock_data",  # 唯一标识符
        name="refresh_stock_data",  # 任务名称
        next_run_time=now  # 立即执行一次
    )
    # 立即返回响应
    return jsonify({"message": "loading"}), 202  # 202表示请求已接受但未处理完毕
