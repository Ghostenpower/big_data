from pyspark.sql import SparkSession, Window
from pyspark.sql.functions import col, countDistinct, row_number, when
from pyspark.sql import functions as F


def test(num):
    print(f"test{num}")


def update_stock_data():
    return "update_data"


def get_stock_data(symbol):
    return "get_data"


def merge_data():
    # 创建 Spark 会话
    spark = SparkSession.builder \
        .appName("StockDataCleaning") \
        .config("spark.driver.memory", "4g") \
        .config("spark.executor.memory", "8g") \
        .getOrCreate()

    # 读取 Hadoop 上的 CSV 文件
    stock_data = spark.read.csv('/user/Ghosten/stock_data.csv', header=True, inferSchema=True)
    stock_index = spark.read.csv('/user/Ghosten/stock_index.csv', header=True, inferSchema=True)
    stock_info = spark.read.csv('/user/Ghosten/stock_info.csv', header=True, inferSchema=True)

    # stock_index pe列若为空则补为0
    stock_index = stock_index.withColumn("pe", when(col("pe").isNull(), 0).otherwise(col("pe")))

    # 合并 DataFrame，按 'trade_date' 和 'ts_code' 列
    merged_data = stock_data.join(stock_index, on=['trade_date', 'ts_code'], how='inner')
    merged_data = merged_data.join(stock_info, on='ts_code', how='inner')

    # 按 'ts_code' 和 'trade_date' 排序
    merged_data = merged_data.orderBy(['ts_code', 'trade_date'])

    # 将合并后的数据保存到 Hadoop HDFS
    output_path = '/user/Ghosten/merged_stock_data.csv'
    merged_data.write.option("header", "true").mode("overwrite").csv(output_path)

    print(f"合并后的数据已保存到 HDFS 路径: {output_path}")

    # 停止 Spark 会话
    spark.stop()


# cleaned_merged_stock_data2_0 stock_daily_data
def clean_stock_daily_data():
    # 初始化Spark会话
    spark = SparkSession.builder \
        .appName("StockDataCleaning") \
        .config("spark.driver.memory", "4g") \
        .config("spark.executor.memory", "8g") \
        .getOrCreate()

    # 读取CSV文件
    df = spark.read.csv("/user/Ghosten/merged_stock_data.csv", header=True, inferSchema=True)

    # 筛选成交量大于等于 1000 的股票
    df_filtered_by_vol = df.filter(col('vol') >= 1000)

    # 计算每只股票的交易日数
    df_stock_days = df_filtered_by_vol.groupBy('ts_code').agg(countDistinct('trade_date').alias('trading_days'))

    # 过滤掉交易日数少于 30 天的股票
    df_stock_days_filtered = df_stock_days.filter(col('trading_days') >= 30)

    # 将筛选后的股票 ID 连接回原始数据，保留符合条件的股票记录
    df_final = df_filtered_by_vol.join(df_stock_days_filtered, on='ts_code', how='inner')

    # 删除trading_days列
    df_final = df_final.drop('trading_days')

    # 使用Spark保存数据为CSV文件，保存到HDFS
    output_path = "/user/Ghosten/cleaned_merged_stock_data2_0.csv"
    df_final.write.option("header", "true").mode("overwrite").csv(output_path)
    print(f"清洗后的数据已保存到 HDFS 路径: {output_path}")

    # 停止Spark会话
    spark.stop()


# df_5day_avg_vol_and_ratio stock_info
# sector_stock_count_and_vol_ratio_over_2 sector_stock_count_and_vol_ratio_over_2
def vol_quantiy_ratio():
    """
    计算量比，即当天的成交量与过去5天的平均成交量的比值，并计算每个行业中量比超过2的股票数量。
    """
    # 初始化Spark会话
    spark = SparkSession.builder \
        .appName("StockVolumeRatio") \
        .config("spark.driver.memory", "4g") \
        .config("spark.executor.memory", "8g") \
        .getOrCreate()

    # 读取CSV文件，假设CSV文件路径为 'path_to_file'
    df = spark.read.csv("/user/Ghosten/cleaned_merged_stock_data2_0.csv",
                        header=True, inferSchema=True)

    # 确保日期格式正确
    df = df.withColumn("trade_date", F.to_date(df["trade_date"], "yyyyMMdd"))

    # 获取每只股票最新一天的交易数据
    window_spec = Window.partitionBy("ts_code").orderBy(F.col("trade_date").desc())
    df_latest_1 = df.withColumn("row_num", F.row_number().over(window_spec)) \
        .filter(F.col("row_num") == 1) \
        .drop("row_num")

    # 获取每只股票过去五天的交易数据
    window_spec_5 = Window.partitionBy("ts_code").orderBy(F.col("trade_date").desc())
    df_past_5 = df.withColumn("row_num", F.row_number().over(window_spec_5)) \
        .filter(F.col("row_num") <= 10) \
        .drop("row_num")

    # 计算每只股票过去五天的平均交易量
    df_avg_vol_5 = df_past_5.groupBy("ts_code").agg(
        F.avg("vol").alias("avg_vol_5")  # 计算过去五天的平均成交量
    )

    # 将每只股票的最新一天数据和过去五天的平均成交量进行连接
    df_with_avg_vol_5 = df_latest_1.join(df_avg_vol_5, on="ts_code", how="left")

    # 计算量比 = 最新一天的交易量 / 过去五天的平均交易量
    df_with_avg_vol_5 = df_with_avg_vol_5.withColumn(
        "volume_ratio", F.col("vol") / F.col("avg_vol_5")
    )

    # 将Spark DataFrame转换为Pandas DataFrame并保存，只保存ts_code、name、trade_date、vol、avg_vol_5、volume_ratio、industry字段
    result1 = df_with_avg_vol_5.select("ts_code", "name", "trade_date", "vol", "avg_vol_5", "volume_ratio",
                                       "industry")

    # vol 转化成保留两位小数
    # result1 = result1.withColumn("vol", F.round(result1["vol"], 2))

    output_path = "/user/Ghosten/output/df_5day_avg_vol_and_ratio.csv"
    result1.write.option("header", "true").mode("overwrite").csv(output_path)
    print(f"Results saved to {output_path}")

    # 按行业聚合，计算每个行业的股票数量
    df_industry_count = df_with_avg_vol_5.groupBy("industry").agg(
        F.count("ts_code").alias("count")
    )

    # 按行业聚合，然后再计算计算每个行业量比大于2的股票数量
    df_industry_count_high = df_with_avg_vol_5.groupBy("industry").agg(
        F.sum(F.when(F.col("volume_ratio") > 2, 1).otherwise(0)).alias("count_over_2")
    )

    # 合并两个DataFrame
    result_df = df_industry_count.join(df_industry_count_high, on="industry", how="left")

    output_path = "/user/Ghosten/output/sector_stock_count_and_vol_ratio_over_2.csv"
    result_df.write.option("header", "true").mode("overwrite").csv(output_path)
    print(f"Results saved to {output_path}")

    # 关闭Spark会话
    spark.stop()


# stock_avg_increase stock_info
def spark_avg_increase():
    # 初始化Spark会话
    spark = SparkSession.builder \
        .appName("StockDataCleaning") \
        .config("spark.driver.memory", "4g") \
        .config("spark.executor.memory", "8g") \
        .getOrCreate()

    # 读取清洗过的CSV数据
    df = spark.read.csv("/user/Ghosten/cleaned_merged_stock_data2_0.csv",
                        header=True, inferSchema=True).select("ts_code", "pct_chg", "close", "total_mv", "circ_mv")

    # df算出每只股票平均close,total_mv,circ_mv
    window_spec = Window.partitionBy("ts_code")
    df = df.withColumn("avg_close", F.avg("close").over(window_spec)) \
        .withColumn("avg_total_mv", F.avg("total_mv").over(window_spec)) \
        .withColumn("avg_circ_mv", F.avg("circ_mv").over(window_spec)) \
        .withColumn("max_close", F.max("close").over(window_spec)) \
        .withColumn("min_close", F.min("close").over(window_spec)) \
        .withColumn("max_total_mv", F.max("total_mv").over(window_spec)) \
        .withColumn("min_total_mv", F.min("total_mv").over(window_spec)) \
        .withColumn("max_circ_mv", F.max("circ_mv").over(window_spec)) \
        .withColumn("min_circ_mv", F.min("circ_mv").over(window_spec)) \
        .withColumn("close_above_avg", F.when(F.col("close") > F.col("avg_close"), 1).otherwise(0)) \
        .withColumn("close_below_avg", F.when(F.col("close") < F.col("avg_close"), 1).otherwise(0)) \
        .withColumn("total_mv_above_avg", F.when(F.col("total_mv") > F.col("avg_total_mv"), 1).otherwise(0)) \
        .withColumn("total_mv_below_avg", F.when(F.col("total_mv") < F.col("avg_total_mv"), 1).otherwise(0)) \
        .withColumn("circ_mv_above_avg", F.when(F.col("circ_mv") > F.col("avg_circ_mv"), 1).otherwise(0)) \
        .withColumn("circ_mv_below_avg", F.when(F.col("circ_mv") < F.col("avg_circ_mv"), 1).otherwise(0))

    # 股票分组，保留avg_total_mv，avg_close，avg_circ_mv
    df = df.groupBy("ts_code").agg(
        F.avg("avg_close").alias("avg_close"),
        F.max("max_close").alias("max_close"),
        F.min("min_close").alias("min_close"),
        F.sum("close_above_avg").alias("close_above_avg"),
        F.sum("close_below_avg").alias("close_below_avg"),

        F.avg("avg_total_mv").alias("avg_total_mv", ),
        F.max("max_total_mv").alias("max_total_mv"),
        F.min("min_total_mv").alias("min_total_mv"),
        F.sum("total_mv_above_avg").alias("total_mv_above_avg"),
        F.sum("total_mv_below_avg").alias("total_mv_below_avg"),

        F.avg("avg_circ_mv").alias("avg_circ_mv"),
        F.max("max_circ_mv").alias("max_circ_mv"),
        F.min("min_circ_mv").alias("min_circ_mv"),
        F.sum("circ_mv_above_avg").alias("circ_mv_above_avg"),
        F.sum("circ_mv_below_avg").alias("circ_mv_below_avg"),

    )
    # 所有列保留两位小数
    df = df.select(
        F.col("ts_code").alias("ts_code"),
        F.round("avg_close", 2).alias("avg_close"),
        F.round("max_close", 2).alias("max_close"),
        F.round("min_close", 2).alias("min_close"),
        F.round("close_above_avg", 2).alias("close_above_avg"),
        F.round("close_below_avg", 2).alias("close_below_avg"),

        F.round("avg_total_mv", 2).alias("avg_total_mv"),
        F.round("max_total_mv", 2).alias("max_total_mv"),
        F.round("min_total_mv", 2).alias("min_total_mv"),
        F.round("total_mv_above_avg", 2).alias("total_mv_above_avg"),
        F.round("total_mv_below_avg", 2).alias("total_mv_below_avg"),

        F.round("avg_circ_mv", 2).alias("avg_circ_mv"),
        F.round("max_circ_mv", 2).alias("max_circ_mv"),
        F.round("min_circ_mv", 2).alias("min_circ_mv"),
        F.round("circ_mv_above_avg", 2).alias("circ_mv_above_avg"),
        F.round("circ_mv_below_avg", 2).alias("circ_mv_below_avg"),
    )

    # 保存
    output_path = "/user/Ghosten/output/stock_avg_increase.csv"
    df.write.option("header", "true").mode("overwrite").csv(output_path)

    # 反馈
    print("spark_avg_increase")

    # df_pandas = df.toPandas()
    # # 用pymysql更新到mysql big_data库 stock_info表对应的字段中
    # import pymysql
    # try:
    #     conn = pymysql.connect(host='localhost', user='root', password='123456', db='big_data')
    #     cursor = conn.cursor()
    #     for index, row in df_pandas.iterrows():
    #         sql = "UPDATE stock_info SET avg_close = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['avg_close'], row['ts_code']))
    #         sql = "UPDATE stock_info SET max_close = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['max_close'], row['ts_code']))
    #         sql = "UPDATE stock_info SET min_close = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['min_close'], row['ts_code']))
    #         sql = "UPDATE stock_info SET close_above_avg = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['close_above_avg'], row['ts_code']))
    #         sql = "UPDATE stock_info SET close_below_avg = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['close_below_avg'], row['ts_code']))
    #
    #         sql = "UPDATE stock_info SET avg_total_mv = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['avg_total_mv'], row['ts_code']))
    #         sql = "UPDATE stock_info SET max_total_mv = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['max_total_mv'], row['ts_code']))
    #         sql = "UPDATE stock_info SET min_total_mv = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['min_total_mv'], row['ts_code']))
    #         sql = "UPDATE stock_info SET total_mv_above_avg = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['total_mv_above_avg'], row['ts_code']))
    #         sql = "UPDATE stock_info SET total_mv_below_avg = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['total_mv_below_avg'], row['ts_code']))
    #
    #         sql = "UPDATE stock_info SET avg_circ_mv = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['avg_circ_mv'], row['ts_code']))
    #         sql = "UPDATE stock_info SET max_circ_mv = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['max_circ_mv'], row['ts_code']))
    #         sql = "UPDATE stock_info SET min_circ_mv = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['min_circ_mv'], row['ts_code']))
    #         sql = "UPDATE stock_info SET circ_mv_above_avg = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['circ_mv_above_avg'], row['ts_code']))
    #         sql = "UPDATE stock_info SET circ_mv_below_avg = %s WHERE ts_code = %s"
    #         cursor.execute(sql, (row['circ_mv_below_avg'], row['ts_code']))
    #         conn.commit()
    # except Exception as e:
    #     print(e)


# multiFactor_marketCap_valuation_model stock_info
def spark_mutifactor_analysis():
    # 初始化Spark会话
    spark = SparkSession.builder \
        .appName("StockDataCleaning") \
        .config("spark.driver.memory", "4g") \
        .config("spark.executor.memory", "8g") \
        .getOrCreate()

    # 从CSV文件读取股票数据
    file_path = "/user/Ghosten/cleaned_merged_stock_data2_0.csv"  # 文件路径
    stock_df = spark.read.option("header", "true").csv(file_path)

    # 将数据类型转换为适当的类型（假设这些列需要转换为浮动类型）
    def cast_columns(df):
        return df.withColumn("pe", F.col("pe").cast("float")) \
            .withColumn("pb", F.col("pb").cast("float")) \
            .withColumn("ps", F.col("ps").cast("float"))

    stock_df = cast_columns(stock_df)

    # 定义窗口函数，按 'ts_code' 分组，并根据 'trade_date' 排序，选择最新日期的数据
    window_spec = Window.partitionBy("ts_code").orderBy(F.col("trade_date").desc())

    # 使用 row_number 函数给每个分组内的行排序，最新交易日期的行将排在第1位
    latest_stock_data = stock_df.withColumn("row_num", F.row_number().over(window_spec)) \
        .filter(F.col("row_num") == 1) \
        .drop("row_num")  # 删除 'row_num' 列

    # 将结果缓存，以加速后续操作
    latest_stock_data.cache()

    # 直接使用所有股票数据（不进行过滤）
    sorted_stocks_latest = latest_stock_data.orderBy(
        "pe", "pb", "ps", ascending=[True, True, True]
    )

    # 标准化因子：将较低的PE、PB、PS标准化为较大的得分
    normalized_stocks_latest = sorted_stocks_latest.withColumn(
        "normalized_pe", 1 / (F.col("pe") + 1)  # PE较低更好，做反转
    ).withColumn(
        "normalized_pb", 1 / (F.col("pb") + 1)  # PB较低更好，做反转
    ).withColumn(
        "normalized_ps", 1 / (F.col("ps") + 1)  # PS较低更好，做反转
    )

    # 计算综合得分
    weighted_score = (0.4 * F.col("normalized_pe") +
                      0.3 * F.col("normalized_pb") +
                      0.3 * F.col("normalized_ps"))

    # 添加得分列
    stocks_with_score_latest = normalized_stocks_latest.withColumn("score", weighted_score)

    # 按得分排序，选择得分最高的股票
    final_sorted_stocks_latest = stocks_with_score_latest.orderBy("score", ascending=False)

    final_sorted_stocks_latest = final_sorted_stocks_latest.select("ts_code", "pe", "pb", "ps", "score")

    final_sorted_stocks_latest.filter(F.col("ts_code") == '000898.SZ').show()

    final_sorted_stocks_latest.withColumn("score", F.round(F.col("score"), 2))

    # 保存为 CSV 文件
    final_sorted_stocks_latest.write.option("header", True).mode("overwrite").csv(
        "/user/Ghosten/output/multiFactor_marketCap_valuation_model.csv")

    # 释放缓存
    latest_stock_data.unpersist()


# high_heat_count sector_stock_count_and_vol_ratio_over_2
def high_heat_count():
    # 初始化 Spark 会话
    spark = SparkSession.builder \
        .appName("StockDataCleaning") \
        .config("spark.driver.memory", "4g") \
        .config("spark.executor.memory", "8g") \
        .getOrCreate()

    # 读取 CSV 文件到 DataFrame
    df = (spark.read.option("header", "true").csv(
        "file:///D:/Document/pythonProject/test/test/api2/data/cleaned_merged_stock_data2_0.csv"))

    # 转换数据类型（必须转换为数字类型才能进行数学计算）
    df = df.withColumn("trade_date", col("trade_date").cast("int")) \
        .withColumn("close", col("close").cast("double")) \
        .withColumn("pct_chg", col("pct_chg").cast("double"))

    # 定义窗口函数，按每只股票的 trade_date 排序
    window_spec = Window.partitionBy("ts_code").orderBy(col("trade_date").desc())

    # 为每只股票分配行号（按日期倒序）
    df_with_row_number = df.withColumn("row_num", row_number().over(window_spec))

    # 获取每只股票的最新一天（第一条）和最新的第五天（第五条）
    latest_day_df = df_with_row_number.filter(col("row_num") == 1)
    fifth_day_df = df_with_row_number.filter(col("row_num") == 5)

    latest_day_df_1 = latest_day_df.select("ts_code", "close", "industry", "name")
    fifth_day_df_1 = fifth_day_df.select("ts_code", "close")

    # 改名
    latest_day_df_1 = latest_day_df_1.withColumnRenamed("close", "latest_close")
    fifth_day_df_1 = fifth_day_df_1.withColumnRenamed("close", "fifth_close")

    # 合并
    df_combined = latest_day_df_1.join(fifth_day_df_1, "ts_code", "inner")

    # 计算涨幅 保留两位小数
    df_combined = df_combined.withColumn("5day_pct_change",
                                         F.round((F.col("latest_close") / F.col("fifth_close") * 100 - 100), 2))

    # 判断热度（假设涨幅超过 5% 是热度较高）
    df_combined = df_combined.withColumn(
        "heat_level",
        when(col("5day_pct_change") > 5, "高")
        .when(col("5day_pct_change") > 0, "中")
        .otherwise("低")
    )

    # 按行业分组，并统计每组有几个heat_level为高
    grouped_df = df_combined.groupBy("industry").agg(
        F.count(when(col("heat_level") == "高", True)).alias("high_heat_count"))

    df_combined.write.option("header", "true").mode("overwrite").csv("/user/Ghosten/output/high_heat_count.csv")
