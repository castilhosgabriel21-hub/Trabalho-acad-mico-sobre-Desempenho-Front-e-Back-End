async function gerarPdfFrontend() {
    try {
        const start = performance.now(); // Início da contagem

        const response = await fetch("/produtos/json");
        const produtos = await response.json();

        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        doc.setFontSize(16);
        doc.text("Lista de Produtos (Frontend)", 14, 15);

        const headers = [['ID', 'Nome', 'Preço', 'Categoria', 'Fornecedores']];
        const data = produtos.map(p => [
            p.id,
            p.nome,
            `R$ ${p.preco.toFixed(2)}`,
            p.categoria ? p.categoria.nome : "Sem categoria",
            p.fornecedores?.length > 0 ? p.fornecedores.map(f => f.nome).join(", ") : "Nenhum"
        ]);

        doc.autoTable({
            head: headers,
            body: data,
            startY: 25,
            styles: { fontSize: 9 },
            headStyles: { fillColor: [0, 123, 255] }
        });

        doc.save("produtos-frontend.pdf");

        const end = performance.now(); // Fim da contagem
        alert(`PDF gerado no navegador em ${(end - start).toFixed(2)} ms`);

    } catch (error) {
        console.error("Erro ao gerar PDF do frontend:", error);
    }
}

async function baixarPdfBackend() {
    const inicio = performance.now();
    const response = await fetch('/produtos/pdf');

    const tempoGerado = response.headers.get('X-Tempo-Geracao');
    const blob = await response.blob();
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = 'produtos.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    const fim = performance.now();
    const duracaoBackend = (fim - inicio).toFixed(2);

    alert(`PDF gerado em ${duracaoBackend}ms`);
}

