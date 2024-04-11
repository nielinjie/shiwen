package cloud.qingyangyunyun.ai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["cloud.qingyangyunyun"])
class DemoApplication()  {
}


fun main(args: Array<String>) {
    runApplication<DemoApplication>()
}
