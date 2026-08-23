package br.senac.ogham.service;

import br.senac.ogham.model.Document;
import br.senac.ogham.repository.DocumentRepository;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceTest {
    @Test void deveClassificarPeriodo() {
        DocumentService s = new DocumentService(new MemRepo());
        assertEquals("Século XIX ou anterior", s.classificarPeriodo("1820-05-10"));
        assertEquals("Século XX", s.classificarPeriodo("1905-03-22"));
        assertEquals("Século XX", s.classificarPeriodo("2000-01-01"));
        assertEquals("Século XXI", s.classificarPeriodo("2023-01-01"));
    }
    @Test void deveValidarInsercao() {
        MemRepo r = new MemRepo(); DocumentService s = new DocumentService(r);
        Document d = s.inserir(new Document(null,"Teste","Autor",null,"PDF","2023-01-01","teste.pdf","tag"));
        assertNotNull(d.id()); assertEquals(1,r.docs.size());
        assertThrows(IllegalArgumentException.class, () -> s.inserir(new Document(null,"","",null,"PDF",null,"a.pdf","")));
    }
    static class MemRepo implements DocumentRepository {
        List<Document> docs=new ArrayList<>(); int next=1;
        public List<Document> listarTodos(){return docs;}
        public List<Document> pesquisar(String t){return docs.stream().filter(d->d.titulo().contains(t)).toList();}
        public Optional<Document> buscarPorId(int id){return docs.stream().filter(d->d.id()==id).findFirst();}
        public Document inserir(Document d){Document n=new Document(next++,d.titulo(),d.autor(),d.descricao(),d.tipo(),d.data(),d.arquivoPath(),d.tags());docs.add(n);return n;}
    }
}
