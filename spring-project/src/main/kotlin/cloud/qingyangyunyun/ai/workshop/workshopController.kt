package cloud.qingyangyunyun.ai.workshop

import kotlinx.serialization.Serializable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WorkspaceController {
    @PostMapping("/api/workspace")
    fun save(@RequestBody workspace: Workspace) {
        WorkshopService().save(workspace)
    }

    @GetMapping("/api/workspace")
    fun load(): Workspace {
        return WorkshopService().load()
    }


}

@Serializable
data class Workspace(
    val variable: String, val prompt: String, val tasks: List<Task>

)

@Serializable
data class Task(
    val result: String,
    val runner:String
)
