package br.com.tools.acessos.serasa.https;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.EncodingUtil;

import br.com.tools.acessos.http.SSLSocket;
import br.com.tools.acessos.serasa.util.SerasaUtil;

/**
 * @brief Implementacao da classe de acesso ao WSDL do SERASA via HTTPS - conexao segura
 * @author ccsilva (reengenharia)
 *
 */
public class SerasaHttps {
	// controle do fim de trasnmissao
	private static final String SERASA_REGISTRO_T999 = "T999";

	/**
	 * 
	 * @param propertiesDir
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String acessoSerasaHttps(String dataDir, String str) throws Exception {
		org.apache.log4j.Logger logger = SerasaUtil.getLogger();		
		logger.debug( ">>SerasaHttpsImpl.SerasaHttps");

		// nome padrao para arquivo propriedade SERASA-PF
		String accessProperties = dataDir + "/resources/https.properties";
		// inputstream para o arquivo de propriedades
		Properties pArquivo;
		InputStream iProp = new BufferedInputStream(new FileInputStream(accessProperties));
		pArquivo = new Properties();
		try {
			// carrega o arquivo de propriedades
			pArquivo.load(iProp);
		} catch (Exception e) {
			throw new FileNotFoundException("Erro abrindo 'serasa.properties' em acessoSerasaHttps()");
		}

		//System.out.println(">>SerasaHttpsImpl.SerasaHttps");
		// criando work context
		logger.debug("Criando Protocolo.");
		Protocol myhttps = new Protocol("https", new SSLSocket(pArquivo.getProperty("user.jks"),pArquivo.getProperty("user.cert")), 443);
		HttpClient httpclient = new HttpClient();
		logger.debug("Criando Client.");
		GetMethod httpget = null;
		logger.debug("Criando Method.");
		StringBuffer strBufferRetorno = new StringBuffer();
		String strRetorno = null;
		
		// configura HTTPS (certificados), consulta e trata retorno
		try {
			//System.out.println("Entrando no configura certificado");
			logger.debug("configurando HTTPS (certificados)");
			final String url = pArquivo.getProperty("user.url");
			// defensive programming
			if (url == null)
				throw new Exception("A url de acesso ao SERASA não foi informada nos parâmetros do sistema (user.url)");

			logger.debug("Url do Serasa: [" + url + "]");
			// configura protocolo para HTTPS (porta 443)
			httpclient.getHostConfiguration().setHost(url, 443, myhttps);

			// Recuperando o username para ser reenviado caso a string não esteja completa.
			final String username = str.substring(0, str.indexOf("B49C"));
			logger.debug("Username utilizado: [" + username + "]");
			// Recuperando o T999 para ser reenviado caso a string não esteja completa.
			final String t999 = str.substring(str.indexOf(SERASA_REGISTRO_T999), str.length());
			logger.debug("String enviada ao Serasa=[" + str + "]");
			// controle das partes de retorno
			int parte = 0;
			// Parametro da Aplicação.
			final String path = pArquivo.getProperty("user.root");
			logger.debug("Root de acesso ao Serasa=[" + path + "]");
			// deve existir um contexto pra acesso ao WSDL do SERASA
			if (path == null)
				throw new Exception("O root do contexto de acesso ao SERASA não informado (user.root)");
			// loop de leitura
			do {
				NameValuePair pair = new NameValuePair("p", str);
				httpget = new GetMethod();
				// configura o path do contexto
				httpget.setPath(path);
				// importante! codifica a string pra UTF-8
				httpget.setQueryString(EncodingUtil.formUrlEncode(new NameValuePair[] { pair }, "UTF-8"));
				// executa a transmissao/metodo
				httpclient.executeMethod(httpget);
				// pega a string de retorno
				strRetorno = inputStreamToString(httpget.getResponseBodyAsStream());

				logger.debug("Retorno do Serasa, Parte: " + ++parte + ", String: [" + strRetorno + "]");
				logger.debug("Verifico se ocorreu erro no acesso ao Serasa");
				// controle do fluxo de transmissao
				if (strRetorno != null && strRetorno.length() > 7 && strRetorno.indexOf(SERASA_REGISTRO_T999) >= 0) {
					if (!"000".equals(strRetorno.substring(strRetorno.indexOf(SERASA_REGISTRO_T999) + 4, strRetorno.indexOf(SERASA_REGISTRO_T999) + 7))) {
						throw new Exception(strRetorno.substring(strRetorno.indexOf(SERASA_REGISTRO_T999)).trim());
					}
				}
				// montagem da string de retorno
				strBufferRetorno.append(strRetorno.substring(400, strRetorno.indexOf(SERASA_REGISTRO_T999)));
				logger.debug("String total obtida no acesso ao Serasa. Parte: " + parte + ", String[" + strBufferRetorno.toString() + "]");
				// Montado a string para ser reenviada.
				str = username + strRetorno.substring(0, 399) + t999;
			// controle de looping
			} while (strRetorno.toUpperCase().indexOf("STRING PARCIAL - HA MAIS REGISTROS A ENVIAR") > 0);
			
			logger.debug("Numero de acesso ao Serasa=[" + parte + "]");
			logger.debug("String final retornada pelo Serasa=[" + strBufferRetorno.toString() + "]");
			
			// retorno a string
			return strBufferRetorno.toString();
		} catch (Throwable t) {
			logger.debug(t.getMessage());
			throw new Exception("Erro no acesso ao Serasa: " + t);
		} finally {
			if (httpget != null)
				httpget.releaseConnection();
			
			logger.debug(">>SerasaHttpsImpl.acessoSerasaHttps");
		}
	}
	
	/**
	 * @brief Transforma a stream de volta numa string "human readable" 
	 * @author ccsilva
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static String inputStreamToString(InputStream inputStream) throws IOException {
		InputStream is = inputStream;
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096]; // tratamento de blocos de 4Kb
		// leia blocos de 4k e monte um buffer
		for (int n; (n = is.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		// retorne buffering
		return out.toString();
	}
}
