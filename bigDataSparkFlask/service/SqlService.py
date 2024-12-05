import pymysql
from pyspark.sql import SparkSession
from pyspark.sql import functions as F
from pyspark.sql.types import DecimalType

# 在模块级别定义连接和游标变量
connection = None
cursor = None
conn = None


def stock_daily_data():
    # 创建SparkSession
    global cursor, connection
    spark = SparkSession.builder \
        .appName("SparkToMySQL") \
        .getOrCreate()

    # 读取数据
    df = spark.read.csv("/user/Ghosten/cleaned_merged_stock_data2_0.csv", header=True, inferSchema=True)

    # 将Spark DataFrame转换为Pandas DataFrame
    df = df.toPandas()

    # 删除包含 NaN 的行
    # 如果你希望去掉所有包含 NaN 的行，使用 dropna()
    df = df.dropna()

    # 或者，使用 fillna() 填充缺失值
    # df = df.fillna(0)  # 可以用0替换NaN，或者你可以根据实际情况使用其他值

    # # 确保数据格式正确（转换为适当的类型）
    # df['trade_date'] = pd.to_datetime(df['trade_date'], format='yyMMdd')

    # 连接到 MySQL 数据库
    try:
        connection = pymysql.connect(
            host='localhost',
            user='root',
            password='123456',
            database='big_data'
        )

        # 确保连接成功
        if connection.open:
            print("stock_daily_data:成功连接到 MySQL 数据库")

        # 创建游标
        cursor = connection.cursor()

        # SQL语句，插入数据到 index_daily 表
        sql = """
                INSERT INTO stock_daily_data
                (ts_code, trade_date, open, high, low, close, pre_close, chg, pct_chg, vol, amount, turnover_rate,
                 pe, pb, ps, total_mv, circ_mv)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """

        # 将每行数据转化为元组并批量插入
        data_to_insert = [
            (
                row['ts_code'], row['trade_date'], row['open'], row['high'], row['low'], row['close'], row['pre_close'],
                row['change'], row['pct_chg'], row['vol'], row['amount'], row['turnover_rate'], row['pe'], row['pb'],
                row['ps'], row['total_mv'], row['circ_mv']
            ) for _, row in df.iterrows()
        ]

        # 执行批量插入
        cursor.executemany(sql, data_to_insert)

        # 提交事务
        connection.commit()
        print(f"stock_daily_data:成功插入 {len(df)} 条数据")

    except pymysql.MySQLError as e:
        print("MySQL 错误:", e)

    finally:
        # 确保关闭游标和连接
        if cursor:
            cursor.close()
        if connection:
            connection.close()
        print("数据库连接已关闭。")


def index_daily():
    # 创建SparkSession
    global cursor, connection
    spark = SparkSession.builder \
        .appName("SparkToMySQL") \
        .getOrCreate()

    # 读取数据
    df = spark.read.csv("/user/Ghosten/index_daily.csv", header=True, inferSchema=True)

    # 将Spark DataFrame转换为Pandas DataFrame
    df = df.toPandas()

    # 删除包含 NaN 的行
    # 如果你希望去掉所有包含 NaN 的行，使用 dropna()
    df = df.dropna()

    # 或者，使用 fillna() 填充缺失值
    # df = df.fillna(0)  # 可以用0替换NaN，或者你可以根据实际情况使用其他值

    # # 确保数据格式正确（转换为适当的类型）
    # df['trade_date'] = pd.to_datetime(df['trade_date'], format='yyMMdd')

    # 连接到 MySQL 数据库
    try:
        connection = pymysql.connect(
            host='localhost',
            user='root',
            password='123456',
            database='big_data'
        )

        # 确保连接成功
        if connection.open:
            print("index_daily:成功连接到 MySQL 数据库")

        # 创建游标
        cursor = connection.cursor()

        # SQL语句，插入数据到 index_daily 表
        sql = """
            INSERT INTO index_daily
            (ts_code, trade_date, close, open, high, low, pre_close, chg, pct_chg, vol, amount)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """

        # 将每行数据转化为元组并批量插入
        data_to_insert = [
            (
                row['ts_code'],
                row['trade_date'],
                row['close'],
                row['open'],
                row['high'],
                row['low'],
                row['pre_close'],
                row['change'],
                row['pct_chg'],
                row['vol'],
                row['amount']
            ) for _, row in df.iterrows()
        ]

        # 执行批量插入
        cursor.executemany(sql, data_to_insert)

        # 提交事务
        connection.commit()
        print(f"index_daily:成功插入 {len(df)} 条数据")

    except pymysql.MySQLError as e:
        print("MySQL 错误:", e)

    finally:
        spark.stop()
        # 确保关闭游标和连接
        if cursor:
            cursor.close()
        if connection:
            connection.close()
        print("数据库连接已关闭。")


def stock_info():
    # 创建SparkSession
    global cursor, connection
    spark = SparkSession.builder \
        .appName("SparkToMySQL") \
        .getOrCreate()

    # 读取数据
    df0 = spark.read.csv("/user/Ghosten/output/df_5day_avg_vol_and_ratio.csv",
                         header=True, inferSchema=True)
    df1 = spark.read.csv("/user/Ghosten/output/stock_avg_increase.csv",
                         header=True, inferSchema=True)
    df2 = spark.read.csv("/user/Ghosten/output/multiFactor_marketCap_valuation_model.csv",
                         header=True, inferSchema=True).drop('trade_date')

    df0.show(5)
    df1.show(5)
    df2.filter(df2['ts_code'] == '000898.SZ').show(5)

    df = df0.join(df1, on='ts_code', how='inner').join(df2, on='ts_code', how='inner')

    df.show(5)

    df.filter(df['ts_code'] == '000898.SZ').show()
    # 将Spark DataFrame转换为Pandas DataFrame
    df = df.toPandas()

    # 删除包含 NaN 的行
    # 如果你希望去掉所有包含 NaN 的行，使用 dropna()
    df = df.dropna()

    # 或者，使用 fillna() 填充缺失值
    # df = df.fillna(0)  # 可以用0替换NaN，或者你可以根据实际情况使用其他值

    # # 确保数据格式正确（转换为适当的类型）
    # df['trade_date'] = pd.to_datetime(df['trade_date'], format='yyMMdd')

    # 连接到 MySQL 数据库
    try:
        connection = pymysql.connect(
            host='localhost',
            user='root',
            password='123456',
            database='big_data'
        )

        # 确保连接成功
        if connection.open:
            print("stock_info:成功连接到 MySQL 数据库")

        # 创建游标
        cursor = connection.cursor()

        # SQL语句，插入数据到 index_daily 表
        sql = """
                INSERT INTO stock_info
                (ts_code, name, trade_date,vol,avg_vol_5,volume_ratio,
                 industry,pe,pb,ps,score,avg_close,
                 max_close,min_close,close_above_avg,close_below_avg,avg_total_mv,max_total_mv,
                min_total_mv,total_mv_above_avg,total_mv_below_avg,avg_circ_mv,max_circ_mv,min_circ_mv,
                circ_mv_above_avg,circ_mv_below_avg)
                VALUES (%s, %s, %s, %s, %s, %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
            """

        # 将每行数据转化为元组并批量插入
        data_to_insert = [
            (
                row['ts_code'],
                row['name'],
                row['trade_date'],
                row['vol'],
                row['avg_vol_5'],
                row['volume_ratio'],
                row['industry'],
                row['pe'],
                row['pb'],
                row['ps'],
                row['score'],
                row['avg_close'],
                row['max_close'],
                row['min_close'],
                row['close_above_avg'],
                row['close_below_avg'],
                row['avg_total_mv'],
                row['max_total_mv'],
                row['min_total_mv'],
                row['total_mv_above_avg'],
                row['total_mv_below_avg'],
                row['avg_circ_mv'],
                row['max_circ_mv'],
                row['min_circ_mv'],
                row['circ_mv_above_avg'],
                row['circ_mv_below_avg']
            ) for _, row in df.iterrows()
        ]

        # 执行批量插入
        cursor.executemany(sql, data_to_insert)

        # 提交事务
        connection.commit()
        print(f"stock_info:成功插入 {len(df)} 条数据")

    except pymysql.MySQLError as e:
        print("MySQL 错误:", e)

    finally:
        # 确保关闭游标和连接
        if cursor:
            cursor.close()
        if connection:
            connection.close()
        print("数据库连接已关闭。")


def sector_stock_count():
    global cursor, conn
    spark = SparkSession.builder \
        .appName("SparkToMySQL") \
        .getOrCreate()

    # 读取数据
    df = spark.read.csv("/user/Ghosten/sector_stock_count_and_vol_ratio_over_2.csv", header=True, inferSchema=True)
    grouped_df_pandas = df.toPandas()

    try:
        conn = pymysql.connect(host='localhost', user='root', password='123456', db='big_data')
        cursor = conn.cursor()
        for index, row in grouped_df_pandas.iterrows():
            insert_sql = """
                    INSERT INTO sector_stock_count_and_vol_ratio_over_2
                    (industry, count, count_over_2)
                    VALUES (%s, %s, %s)
                """
            data_to_insert = [
                (row['industry'], row['count'], row['count_over_2'])
                for _, row in df.iterrows()
            ]

            # 执行批量插入
            cursor.executemany(insert_sql, data_to_insert)
            conn.commit()
            print(f"成功更新 {row['industry']} 行业")
    except pymysql.MySQLError as e:
        print("MySQL 错误:", e)

    finally:
        cursor.close()
        spark.stop()
        conn.close()
        print("Spark会话和数据库连接已关闭")
        print("程序结束")


def high_heat_count():
    global cursor, conn
    spark = SparkSession.builder \
        .appName("SparkToMySQL") \
        .getOrCreate()

    # 读取数据
    df = spark.read.csv("/user/Ghosten/high_heat_count.csv", header=True, inferSchema=True)
    grouped_df_pandas = df.toPandas()

    try:
        conn = pymysql.connect(host='localhost', user='root', password='123456', db='big_data')
        cursor = conn.cursor()
        for index, row in grouped_df_pandas.iterrows():
            sql = "UPDATE sector_stock_count_and_vol_ratio_over_2 SET high_heat_count = %s WHERE industry = %s"
            cursor.execute(sql, (row['high_heat_count'], row['industry']))
            conn.commit()
            print(f"成功更新 {row['industry']} 行业")
    except pymysql.MySQLError as e:
        print("MySQL 错误:", e)

    finally:
        cursor.close()
        spark.stop()
        conn.close()
        print("Spark会话和数据库连接已关闭")
        print("程序结束")
