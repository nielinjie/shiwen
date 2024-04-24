<script setup lang='ts'>
import { useRecordStore, type RecordItem } from '@/stores/recordStore';
import { storeToRefs } from 'pinia';
import { List ,ListItem} from 'ant-design-vue';
import { computed } from 'vue';


    const rs  = useRecordStore()
    const {records} = storeToRefs(rs)
    const recordBriefs = computed(() => {
        return records.value.map((record: RecordItem) => ({
            clientName: record.clientName,
            ...record.brief,
        }));
    });
</script>


<template>
    <List size="large" bordered :dataSource="recordBriefs">
        <template #renderItem="{ item }">
            <ListItem :item="item">{{ item }}</ListItem>
        </template>
    </List>
</template>
<style scoped>

</style>