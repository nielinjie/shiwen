package cloud.qingyangyunyun.ai.workshop

import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WorkspaceController(@Autowired val workshopService: WorkshopService) {
    @PostMapping("/api/workspace")
    fun save(@RequestBody workspace: Workspace) {
        workshopService.save(workspace)
    }

    @GetMapping("/api/workspace")
    fun load(): Workspace {
        return workshopService.load()
    }


}

@Serializable
data class Workspace(
    val variables: List<String>, val prompts: List<String>, val cells: List<WorkCell>

)

@Serializable
data class Task(
    val result: String,
    val client:String
)
@Serializable
data class WorkCell(val x:Int,val y:Int,val tasks: List<Task>)