package com.example.SpringAula2.service;

import com.example.SpringAula2.model.Produto;
import com.example.SpringAula2.repository.ProdutoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoPdfService {

    @Autowired
    private ProdutoRepository repository;

    public void gerarPdf(HttpServletResponse response) throws Exception {

        List<Produto> produtos = repository.findAll(Sort.by("id").ascending());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=produtos.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Paragraph titulo = new Paragraph("LISTA DE PRODUTOS",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);

        document.add(titulo);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        table.addCell("ID");
        table.addCell("Nome");
        table.addCell("Preço");

        for (Produto p : produtos) {
            table.addCell(String.valueOf(p.getId()));
            table.addCell(p.getNome());
            table.addCell(String.valueOf(p.getPreco()));
        }

        document.add(table);
        document.close();
    }
}