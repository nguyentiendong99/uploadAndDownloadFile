package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DemoApplicationTests {
    @Autowired
    private DocumentRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testInsertDocument() throws IOException {
        File file = new File("C:\\Users\\Laptop88\\Downloads\\Css-Selector-Basic.docx");
        Document document = new Document();
        document.setName(file.getName());
        byte[] bytes = Files.readAllBytes(file.toPath());
        document.setContent(bytes);
        long fileSize = bytes.length;
        document.setSize(fileSize);
        Document savedDoc = repository.save(document);
        Document exitDoc = entityManager.find(Document.class , savedDoc.getId());
    }

}
