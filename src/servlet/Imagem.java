package servlet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Imagem
 */
@WebServlet("/imagem")
public class Imagem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reqImg = request.getParameter("img");
		String url = "c:\\windows\\temp\\";
		
		File img = new File(url  + reqImg + ".png");
		if (img.exists()) {
			response.setContentType("image/png");
			
			try {
				//Lê a imagem
				BufferedImage biPng = ImageIO.read(img);
				//Conserta a transparência para ser convertida para .jpg
				BufferedImage biJpg = new BufferedImage(biPng.getWidth(), biPng.getHeight(), BufferedImage.TYPE_INT_RGB);
				biJpg.getGraphics().drawImage(biPng, 0, 0, biPng.getWidth(), biPng.getHeight(), Color.white, null);
				
				//Cropa uma zona de 100x100 no centro da imagem
				int novaImagemW = 100, novaImagemH = 100;
				int cropX = Math.round(biJpg.getWidth() / 2) - (novaImagemW / 2);
				int cropY = Math.round(biJpg.getHeight() / 2) - (novaImagemH / 2);
				BufferedImage novaImagem = biJpg.getSubimage(cropX, cropY, novaImagemW, novaImagemH);
				
				//Cria a pasta de imagens se ela não existir
				File pastaDeImagens = new File(url);
				if (!pastaDeImagens.exists()) pastaDeImagens.mkdir();
				//Grava a nova imagem no disco como sufixo crop
				File cImg = new File(url + reqImg + "-crop" + ".jpg");
				ImageIO.write(novaImagem, "jpg", cImg);
				
				//Envia a imagem como resposta para o GET
				OutputStream os = response.getOutputStream();
				ImageIO.write(novaImagem, "jpg", os);
			} catch (Exception e) {
				response.getWriter().append(e.getMessage());
			}
		} else {
			response.getWriter().append("Imagem não encontrada. Insira um parâmetro GET para imagem.");
		}
		
	}

}
