package com.github.hiwepy.huaweiai.pangu;

import com.huaweicloud.pangu.dev.sdk.api.embedings.Embeddings;
import com.huaweicloud.pangu.dev.sdk.api.llms.LLMs;
import com.huaweicloud.pangu.dev.sdk.api.memory.bo.Document;
import com.huaweicloud.pangu.dev.sdk.api.memory.config.VectorStoreConfig;
import com.huaweicloud.pangu.dev.sdk.api.memory.vector.Vector;
import com.huaweicloud.pangu.dev.sdk.api.memory.vector.Vectors;
import com.huaweicloud.pangu.dev.sdk.api.skill.Skills;
import com.huaweicloud.pangu.dev.sdk.skill.DocSkill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class HuaweiAiPanguDocClient {

    private final static Logger logger = LoggerFactory.getLogger(HuaweiAiPanguDocClient.class);

    private final DocSkill docSkill;

    public final RetryTemplate retryTemplate;

    public HuaweiAiPanguDocClient(DocSkill docSkill, RetryTemplate retryTemplate) {
        Assert.notNull(docSkill, "DocSkill must not be null");
        Assert.notNull(retryTemplate, "retryTemplate must not be null");
        this.docSkill = docSkill;
        this.retryTemplate = retryTemplate;
    }

    public ResponseEntity<MoonshotAiFileApi.MoonshotAiFileResponse> docAsk() {

        Vector cssVector = Vectors.of(Vectors.CSS,
                VectorStoreConfig.builder()
                        .embedding(Embeddings.of(Embeddings.CSS))
                        .indexName("test-stuff-document-062102")
                        .build());

        // 检索
        String query = "杜甫的诗代表了什么主义诗歌艺术的高峰？";
        List<Document> docs = cssVector.similaritySearch(query, 4, 105);

// 问答
        DocSkill docSkill = Skills.Document.newDocAskStuffSkill(LLMs.of(LLMs.PANGU));

        System.out.println(docSkill.executeWithDocs(docs, query));

        return retryTemplate.execute(context -> {
            logger.debug("Listing files");
            return moonshotAiFileApi.listFile();
        });
    }


}
