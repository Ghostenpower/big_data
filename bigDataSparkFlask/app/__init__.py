from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask, render_template, g
import pymysql

from config.SparkConfig import SparkConfig
from service import SparkService as sparkService

# 安装 PyMySQL 作为 MySQLdb
pymysql.install_as_MySQLdb()


def create_app():
    app = Flask(__name__, template_folder='./../templates')

    # 初始化调度器
    scheduler = BackgroundScheduler()

    # 配置调度任务
    # scheduler.add_job(sparkService.test, 'interval', args=["2"], seconds=10,
    #                   id="test", name="test2024")
    scheduler.start()

    # 将调度器绑定到 Flask 应用的上下文中
    app.before_request(lambda: setattr(g, 'scheduler', scheduler))

    @app.errorhandler(404)
    def not_found(error):
        print("404 error handler triggered")  # 测试用
        return render_template('404.html'), 404

    # 注册蓝图
    from .routes import main
    app.register_blueprint(main)

    return app
