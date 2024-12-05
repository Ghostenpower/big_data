<template>
    <el-form :model="form" label-width="auto">
        <el-row justify="center" :gutter="10">
            <el-col :span="6">
                <el-form-item label="股票代码">
                    <el-input v-model="form.ts_code" placeholder="请输入股票代码" clearable
                        :disabled="formJudge[1] || formJudge[2]" />
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label="股票名称">
                    <el-input v-model="form.name" placeholder="请输入股票名称" clearable
                        :disabled="formJudge[0] || formJudge[2]" />
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label="股票行业">
                    <el-input v-model="form.industry" placeholder="请输入股票行业" clearable
                        :disabled="formJudge[0] || formJudge[1]" />
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label=" ">
                    <el-button type="primary" @click="getStockInfos()">搜索</el-button>
                    <el-popover :visible="deleteVisible" placement="top" :width="160">
                        <p>确定要清空吗?</p>
                        <div style="text-align: right; margin: 0">
                            <el-button size="small" text @click="deleteVisible = false">取消</el-button>
                            <el-button size="small" type="danger" @click="clear(); deleteVisible = false">
                                确定
                            </el-button>
                        </div>
                        <template #reference>
                            <el-button @click="deleteVisible = true">清空</el-button>
                        </template>
                    </el-popover>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
    <el-row justify="center">
        <el-col align="middle" :span="18">
            <el-table v-loading="tableLoading" :data="tableData" style="width: 100%" height="460px">
                <el-table-column prop="ts_code" label="股票代码" sortable  width="180" />
                <el-table-column prop="name" label="股票名称" sortable  width="180" />
                <el-table-column prop="industry" label="所属行业" />
                <el-table-column fixed="right" label="操作" min-width="120">
                    <template #default="{ row }">
                        <el-button link type="primary" size="small" @click="handleDetail(row)">
                            详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="20" style="display: flex; justify-content: right;">
            <el-pagination layout="prev, pager, next" :total="stockInfos.length" :page-size="30"
                v-model:current-page="currentPage" />
        </el-col>
    </el-row>
</template>

<script lang="ts" setup>
import { reactive, ref, computed, onMounted, onBeforeMount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOneStockInfo, getStockInfoByIndustry } from '../axios/api'
const route = useRoute()
const router = useRouter()

// do not use same name with ref
const form = reactive({
    ts_code: '',
    name: '',
    industry: ''
})

const tableLoading = ref(false)
const stockInfos = reactive([])

const pageSize = 30;
const totalPage = computed(() => {
    // 计算总页数，使用 Math.ceil 向上取整
    const pageCount = Math.ceil(stockInfos.length / pageSize);
    return pageCount > 0 ? pageCount : 1;  // 如果没有数据，确保至少有一页
});
const currentPage = ref(1)
const currentPageData = computed(() => {
    if (stockInfos.length == 0) return stockInfos.slice(0, 0);
    const start = (currentPage.value - 1) * pageSize;
    const end = start + pageSize;
    return stockInfos.slice(start, end); // 返回当前页的数据
})
onBeforeMount(async () => {
    try {
        tableLoading.value = true
        const response = await getStockInfoByIndustry(form)
        stockInfos.length = 0; // 清空原数组
        stockInfos.push(...response.data); // 展开数组并插入新的元素
    } catch (error) {
        console.error('获取股票数据失败', error);  // 错误处理
    } finally {
        tableLoading.value = false
    }
})

const formJudge = computed(() => {
    return [
        !!form.ts_code,      // 如果form.code是空字符串、null、undefined等，返回false
        !!form.name,
        !!form.industry
    ];
});

const onSubmit = () => {
    console.log('submit!')
}
const deleteVisible = ref(false);
const clear = () => {
    Object.assign(form, {
        code: '',
        name: '',
        industry: ''
    });
}
const getStockInfos = async () => {
    try {
        tableLoading.value = true
        let response
        if (form.industry == "" && form.name == "" && form.ts_code == "") {
            response = await getStockInfoByIndustry(form)
        } else if (form.industry == "") {
            response = await getOneStockInfo(form)
        } else {
            response = await getStockInfoByIndustry(form)
        }

        stockInfos.length = 0; // 清空原数组
        if (Array.isArray(response.data)) {
            stockInfos.push(...response.data);  // 如果是数组，则展开并推入
        } else if (response.data != null) {
            stockInfos.push(response.data);  // 如果是非空对象，则直接推入
        }
        currentPage.value = 1;
    } catch (error) {
        console.error('获取股票数据失败', error);  // 错误处理
    } finally {
        tableLoading.value = false
    }
}

const handleDetail = (row) => {
    const ts_code = row.ts_code;
    
    // 通过 Vue Router 获取查询参数
    const queryParams = new URLSearchParams(row).toString();
    
    // 使用 window.open() 打开新标签页，跳转到详情页
    window.open(router.resolve({ name: 'stockdetail', query: row }).href, '_blank');
}

const tableData = computed(() => currentPageData.value)
</script>