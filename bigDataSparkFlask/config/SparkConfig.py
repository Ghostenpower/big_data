import os

from pyspark import SparkConf
from pyspark.sql import SparkSession


class SparkConfig:
    # 从环境变量中读取默认配置
    DEFAULT_CONFIG = {
        'master': os.getenv('SPARK_MASTER', 'local[*]'),
        'app_name': os.getenv('SPARK_APP_NAME', 'DefaultSparkApp'),
    }

    def __init__(self):
        self.config = SparkConf().setAppName("BigDataSparkFlask").setMaster("local[*]")

    def get_config(self):
        return self.config

    def create_spark_session(self):
        return SparkSession.builder.config(conf=self.config).getOrCreate()