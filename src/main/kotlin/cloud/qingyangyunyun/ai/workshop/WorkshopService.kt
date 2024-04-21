package cloud.qingyangyunyun.ai.workshop

import cloud.qingyangyunyun.ai.JsonData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class WorkshopService(@Autowired val paths: Paths) {
    val stateData = JsonData(paths.statePath)
    fun save(workspace: Workspace) {
        stateData.save(workspace)
    }

    fun load(): Workspace {
        return stateData.load(Workspace(listOf(), listOf(), listOf()))
    }
}