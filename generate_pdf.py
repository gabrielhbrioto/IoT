from fpdf import FPDF

# Caminho para o diretório das imagens
image_dir = "Images/"

# Mapeamento das imagens
images = {
    "login": image_dir + "login.jpg",
    "home": image_dir + "home.jpg",
    "criar_sala": image_dir + "criar-sala.png",
    "sala_criada": image_dir + "sala-recem-criada.png",
    "sala_listada": image_dir + "sala-listada.jpg",
    "sala": image_dir + "sala.jpg"
}

# Inicializa o PDF
pdf = FPDF()
pdf.set_auto_page_break(auto=True, margin=15)
pdf.add_page()
pdf.set_font("Arial", size=12)

# Adiciona conteúdo ao PDF
pdf.set_font("Arial", style="B", size=14)
pdf.cell(200, 10, txt="Sistema de Monitoramento e Controle de Iluminação Inteligente", ln=True, align='C')

pdf.set_font("Arial", size=12)
pdf.ln(10)
pdf.multi_cell(0, 10, """
Este projeto implementa um sistema IoT para monitorar e controlar a iluminação em ambientes internos, como escritórios, residências ou salas de aula, utilizando sensores e microcontroladores. A solução visa eficiência energética, conforto dos usuários e facilidade de controle via interface web.
""")

# Adiciona imagens ao PDF
pdf.add_page()
pdf.set_font("Arial", style="B", size=12)
pdf.cell(0, 10, "Tela de Login", ln=True)
pdf.image(images["login"], w=150)

pdf.add_page()
pdf.cell(0, 10, "Página Inicial", ln=True)
pdf.image(images["home"], w=150)

pdf.add_page()
pdf.cell(0, 10, "Criar Sala", ln=True)
pdf.image(images["criar_sala"], w=150)

pdf.add_page()
pdf.cell(0, 10, "Sala Criada", ln=True)
pdf.image(images["sala_criada"], w=150)

pdf.add_page()
pdf.cell(0, 10, "Sala Listada", ln=True)
pdf.image(images["sala_listada"], w=150)

pdf.add_page()
pdf.cell(0, 10, "Detalhes da Sala", ln=True)
pdf.image(images["sala"], w=150)

# Salva o PDF
pdf.output("Sistema_Iluminacao_Inteligente.pdf")

print("PDF gerado com sucesso!")
