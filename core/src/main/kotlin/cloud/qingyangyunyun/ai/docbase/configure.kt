package cloud.qingyangyunyun.ai.docbase

import cloud.qingyangyunyun.ai.embedding.EmbeddingIndexer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.nietongxue.docbase.SegmentMethod
import xyz.nietongxue.docbase.filetypes.FileType
import xyz.nietongxue.docbase.filetypes.Md
import xyz.nietongxue.docbase.filetypes.Txt


class Indexers(val map: Map<String, Indexer>) : Map<String, Indexer> by map {
}

class SegmentMethods(val map: Map<String, SegmentMethod>) : Map<String, SegmentMethod> by map {
}

class FileTypes(val map: Map<String, FileType>) : Map<String, FileType> by map {

}

@Configuration
class DocbaseConfiguration {


    @Bean
    fun indexers(@Autowired embeddingIndexer: EmbeddingIndexer): Indexers {
        return Indexers(mapOf(
            "Simple" to SimpleIndexer(),
            "Embedding" to embeddingIndexer))
    }

    @Bean
    fun segmentMethods(): SegmentMethods {
        return SegmentMethods(mapOf(
//            "Whole" to SegmentMethod.WholeSegment,
            "Paragraph" to SegmentMethod.ParagraphSegment,
            "Line" to SegmentMethod.LineSegment))
    }

    @Bean
    fun fileTypes(): FileTypes {
        return FileTypes(mapOf(
            "Txt" to Txt(),
            "MD" to Md(),
        ))
    }
}