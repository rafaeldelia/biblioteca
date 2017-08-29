package br.com.tools.acessos.serasa.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import br.com.tools.acessos.exception.LayoutException;
import br.com.tools.acessos.serasa.Registros;
import br.com.tools.acessos.util.Util;
import br.com.tools.acessos.util.UtilData;

/**
 * @brief classe utilitaria para o acesso do SERASA PF e PJ
 * @author ccsilva
 *
 */
public class SerasaUtil {

	/** logger privado da classe */
	private static Logger logger = null;
	
	/**
	 * Cria o Logger padrão para o acesso ao Serasa
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger("br.com.tools.serasa");
		}
		
		return logger;
	}
	
	/**
	 * @brief Metodo para montar um header de login para o SERASA
	 * @param accessProperties
	 * @param serasaLogin
	 * @throws Exception
	 */
	public void fillerLoginSerasa(String accessProperties, StringBuffer serasaLogin ) throws Exception{
		// definicao de contextos locais para logging
		Logger logger = SerasaUtil.getLogger();
		// 1o, o arquivo de properties (serasa.properties)
		//InputStream iProp =  this.getClass().getResourceAsStream("/" + accessProperties);
		Properties pArquivo;
		// tenta carregar o arquivos de propriedades
		try {
			InputStream iProp = new BufferedInputStream(new FileInputStream(accessProperties));
			pArquivo = new Properties();
			// carrega o arquivo de propriedades
			pArquivo.load(iProp);
		    /**
			 * Informacoes de Login
			 */
		    // login
			logger.debug( "******************** Informacoes de Login SERASAPF ********************");
			String sLogin = pArquivo.getProperty("serasa.login");// "99186487";
			//logger.log(Level.FINE, "serasa.login: "+sLogin);
			String sLoginSize = pArquivo.getProperty("serasa.login_size");
			String sLoginType = pArquivo.getProperty("serasa.login_type");
			serasaLogin.append(Util.stringFiller(sLogin, Integer.parseInt(sLoginSize), sLoginType));
			// password
			String sPass = pArquivo.getProperty("serasa.password");
			logger.debug( "serasa.password: "+sPass);
			String sPassSize = pArquivo.getProperty("serasa.pass_size");
			String sPassType = pArquivo.getProperty("serasa.pass_type");
			serasaLogin.append(Util.stringFiller(sPass, Integer.parseInt(sPassSize), sPassType));
			// Tratamento de nova senha
			String nPass = pArquivo.getProperty("serasa.newpass");
			logger.debug( "serasa.newpass: "+nPass);
			String nPassSize = pArquivo.getProperty("serasa.npass_size");
			String nPassType = pArquivo.getProperty("serasa.npass_type");
			serasaLogin.append(Util.stringFiller(nPass, Integer.parseInt(nPassSize), nPassType));
			// show out os parametros de login
			logger.debug( "serasaLogin: "+serasaLogin);
			//System.out.println("serasaLogin: [" + serasaLogin + "]");
		} catch (FileNotFoundException e) {
			throw new Exception("Erro abrindo " + accessProperties + ": " + e.getMessage ());
		} catch (IOException e) {
			throw new Exception("Erro lendo " + accessProperties + ": " + e.getMessage ());
		}
	}

	/**
	 * @brief Monta hash de saida do SERASA; pega a strign de retorno para isso
	 * @param serasaBack
	 * @return Hashmap com os registros
	 * @return HashTable
	 * @throws LayoutException Erro de layout
	 */
	public HashMap<String, Object> builderHashOut( String serasaBack ) throws LayoutException {
		// definicao de contextos locais para logging
		Logger log = SerasaUtil.getLogger();
		// working context
		int size_brick = 115; // determina o tamanho dos blocos com os registros
		String workBack = ""; // string para trabalhar os bricks
		// hash de retorno com os objetos da consulta ao serasa
		HashMap<String, Object> hashIn = new HashMap<String, Object>();
		log.debug(">> builderHashOut()");
		
		// Mensagem estah preenchida e nao comeca com T999.
		// faremos leitura dessa string, seguindo blocos de size_brick = 115 bytes.
		while (serasaBack != null && !serasaBack.startsWith("T999") && serasaBack.length() > 0) {
			// controle de corte dos blocos para analise dos registros
			if (serasaBack.length() >= size_brick) {
				workBack = serasaBack.substring(0, size_brick); // corta o bloco
				serasaBack = serasaBack.substring(size_brick); // step-in no retorno
			} else { // controle de overflow; isso nao deve ocorrer (defensive programming)
				workBack = serasaBack.substring(0, serasaBack.length());
				serasaBack = null;
			}
			//log.info("[" + workBack + "]");
			// chama metodo de tratamento dos registros por reflexao
    		try {
				// configurando os tipos de objetos para a reflexao usando "generics"
				Class<?> partypes[] = new Class[3]; // controle da assinatura
				partypes[0] = String.class; // 1a eh tipo objeto String
				partypes[1] = HashMap.class; // 2a eh tipo Hashtale
				partypes[2] = Logger.class; // 3o tipo logging
				// configurando qual metodo irah ser chamado
				Class<?> cls = Registros.class; // forcando a classe pelo "generics"
				// observe que sempre o 4 primeiros bytes, contem o indicador do tipo de registro
				// br.com.tools.acessos.serasa.Registros.registro<?>; ex. registroB001
				Method meth = cls.getMethod("registro" + workBack.subSequence(0, 4), partypes);
				// configurando os dados por tipo, conforme classificado
				Object arglist[] = new Object[3]; // criando objeto para a arglist
				arglist[0] = workBack;
				arglist[1] = hashIn;
				arglist[2] = log;
				// instanciando o metodo especifo
				Registros methobj = new Registros();
				// executa o metodo com sua lista de argumentos em reflexao
				meth.invoke(methobj, arglist);
			} catch (SecurityException e) {
				log.error("Erro de Security: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				log.error("Erro de IllegalArgument: " + e.getMessage());
			} catch (NoSuchMethodException e) {
				// quando um registro nao estiver implementado, o processamento continuarah
				log.error("*** Registro '" + workBack.subSequence(0, 4) + "' não implementado ***");
			} catch (IllegalAccessException e) {
				log.error("Erro de IllegalAccess: " + e.getMessage());
			} catch (InvocationTargetException e) {
				log.error("Erro de InvocationTarget: " + e.getMessage());
			} catch (IndexOutOfBoundsException e) {
				log.error("Erro no tratamento do Registro: " + e.getMessage());
				throw new LayoutException("Erro no tratamento do Registro '" 
						+ workBack.subSequence(0, 4) + "'");
			}
		} // fim while()...
		
		log.debug( "<< builderHashOut()");
		return hashIn;
	}

	/**
	 * @brief Metodo para montar a string de requisicao do B49C
	 * @param accessProperties
	 * @param serasaLogin
	 * @throws Exception 
	 */
	public void fillerRequestSerasa(String layoutSerasa, StringBuffer serasaRequest, String sCPF ) 
		throws Exception {
		// definicao de contextos locais para logging
		Logger logger = SerasaUtil.getLogger();
		/**
		 * Layout do Credit Bureau: "B49C" Preencher com "brancos" quando utilizar
		 * porta de comunicacao (Host a Host), ou preencher com "B49C".
		 */
    	try {
			//InputStream iLayout =  this.getClass().getResourceAsStream("/" + "layout_b49c.xml");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(layoutSerasa));
            // normalize a representacao do texto
            doc.getDocumentElement ().normalize ();
            // "field" serah a representacao dos nodes
            NodeList listOfFields = doc.getElementsByTagName("field");
            //int totalFields = listOfFields.getLength();
            //System.out.println("Total no of fields: " + totalFields);
            // varredura da lista de nodes
            for(int s = 0; s < listOfFields.getLength() ; s++){
            	// caminha na lista
                Node fieldNode = listOfFields.item(s);
                // verifica se eh um elemento do node, caso tipo "field"
                if(fieldNode.getNodeType() == Node.ELEMENT_NODE){
                	// posicionamento no node principal
                    Element fieldElement = (Element)fieldNode;
                    // encontra o subnode "name"
                    NodeList nameList = fieldElement.getElementsByTagName("name");
                    Element nameElement = (Element)nameList.item(0);
                    NodeList textNameList = nameElement.getChildNodes();
                    // pega string especial, no caso, preciso do CPF dentro do XML para substituir
                    String specialStr = ((Node)textNameList.item(0)).getNodeValue().trim();
                    if( specialStr.equalsIgnoreCase("cpf")){ // existe no layout_b49c.xml
            			logger.debug( "special name: ["+specialStr + "]");
	                    //System.out.println("special name: [" + specialStr + "]");
	                    specialStr = sCPF; // tem que ser "CPF"
	                    if (specialStr == null)
		                    // IMPORTANTE! se nao encontra na hashIn, gerar saida controlada??
	        				throw new Exception("Erro GRAVE: nao foi encontrado a key 'cpf' na Hash In");
                    }
                    //System.out.println("name: [" + specialStr + "]");
                    // encontra o subnode "size" (tamanho da string)
                    NodeList sizeList = fieldElement.getElementsByTagName("size");
                    Element sizeElement = (Element)sizeList.item(0);
                    // posiciona no elemento do node
                    NodeList textSizeList = sizeElement.getChildNodes();
                    //System.out.println("size: [" + ((Node)textSizeList.item(0)).getNodeValue().trim() + "]");
        			logger.debug( "size: [" + ((Node)textSizeList.item(0)).getNodeValue().trim() + "]");
                    // encontra o subnode "type" (tipo da string)
                    NodeList typeList = fieldElement.getElementsByTagName("type");
                    Element typeElement = (Element)typeList.item(0);
                    NodeList textTypeList = typeElement.getChildNodes();
                    //System.out.println("type: [" + ((Node)textTypeList.item(0)).getNodeValue().trim() + "]");
        			logger.debug( "type: [" + ((Node)textTypeList.item(0)).getNodeValue().trim() + "]");
                    // montagem da string do B49C; muito cuidado na relacao codigo x XML de layout
                    // qualquer deslize e esta montagem nao serah bem sucedida, portanto.
        			serasaRequest.append(Util.stringFiller(specialStr, 
        					Integer.parseInt(((Node)textSizeList.item(0)).getNodeValue().trim()), 
        					((Node)textTypeList.item(0)).getNodeValue().trim()));
                }//fim da clausulaf if
            }//fim do loop for loop com var 's'
        }catch (SAXParseException err) {
        	throw new Exception("** Erro de SAXParsing" + ", linha " 
        			+ err.getLineNumber () + ", uri " + err.getSystemId ());
        }catch (SAXException e) {
        	Exception x = e.getException ();
        	((x == null) ? e : x).printStackTrace ();
			throw new Exception("** Erro no SAX: " + x.getMessage ());
        }catch (IOException t) {
        	//System.out.println("** Erro lendo layout_B49C.xml " + t.getMessage ());
			throw new Exception("Erro lendo " + layoutSerasa + ": " + t.getMessage ());
        } finally {
        	logger.debug( "Leitura de Todo o bloco de montagem de layouts com sucesso");
        }
	}

	/**
	 * @brief metodo para montagem do layout P005
	 * @param accessProperties
	 * @param serasaLogin
	 * @throws Exception 
	 */
	public void fillerLayoutP005(StringBuffer serasaBack, HashMap<String, Object> hashIn ) 
		throws Exception {
		// definicao de contextos locais para logging
		Logger logger = SerasaUtil.getLogger();
		// preenchimento do P005
		try {
			serasaBack.append(Util.stringFiller("P005", 4, "S"));
			//informando "S", entao todos os campos do registros devem estar preenchidos
			serasaBack.append("S");
			// pega o CMC7 composto por:
			// - codigo de compensacao do banco
			// - numero da agencia bancaria
			// - digito modulo 10 dos campos
			// - codigo da camera de compensacao
			// - numero do cheque
			// - codigo de tipicacao do documento (5, 6, 7 e 8)
			// - digito modulo 10 dos campos banco e agencia
			// - numero da conta corrente
			// - digito modulo 10 do campo conta
			serasaBack.append(Util.stringFiller((String)hashIn.get("CMC7"), 30, "S"));
			// pego o valor da hash de entrada e transformo num real
			double valorCheque = (Double) hashIn.get("VLR_CHEQUE");
			// o valor deve ser multiplicado por 100 para que os centavos sejam incluidos
			serasaBack.append(Util.stringFiller(String.valueOf((int) (valorCheque * 100)), 12, "N"));
			// Data de vencimento do cheque; deverah vir AAAAMMDD
			// uso o filler como defensive programming; evitar overflow
			serasaBack.append(Util.stringFiller(UtilData.format( (Date) hashIn.get("VENC_CHEQUE"), "yyyyMMdd"), 8, "S"));
			// Tipo de movimento sao:
			// "I"=incluir cheque; "B"=baixar cheque; "E"=baixar cheque que terah 
			// novo cheque incluido na sequencia; "C"=consultar cheque
			// continuo usando o filler, como defensive programing
			serasaBack.append(Util.stringFiller((String)hashIn.get("TIPO_MOV"), 1, "S"));
			// filler
			serasaBack.append(Util.stringFiller("", 59, "S"));
		} catch (NumberFormatException e) {
			logger.debug( "fillerLayoutP005()>> " + e.getMessage ());
			//System.out.println("acessoSerasaHttps() " + e.getMessage ());
			throw new Exception("fillerLayoutP005>> " + e.getMessage ());
		}
	
	}


	public static void main(String[] args) {
	    SerasaUtil serUtil = new SerasaUtil();
	    
	    HashMap<String, Object> hashOut = new HashMap<String, Object>();
	    
	    try {
		hashOut = serUtil.builderHashOut("P006SNSSS9999SSSSSSSS NNNSN                                                                                        B001RAFAEL BUENO LIFUN LIN                       0068268637900000000000000019840821FLA S    530370622220150719   2CB002   2015121519840821SILVANA DE SOUSA BUENO                       M               000000000000000     00000000  CB003            00                                       0000000000000853252340900000000000000000000000000        CB004R GONCALVES LEDO              673  673       CENTRO              FORTALEZA                CE60110261000000    CB3530102016010060020010010000000000000002015121500000000                                                          CB35420160113SUPPLIERCARD ADM CARTOES CREDITO S/A                   00000000006951711                              CB35420160113MINERVA S/A                                            00000000067620377                              CB35420160112SUPPLIERCARD ADM CARTOES CREDITO S/A                   00000000006951711                              CB35420160111SUPPLIERCARD ADM CARTOES CREDITO S/A                   00000000006951711                              CB35420160108SUPPLIERCARD ADM CARTOES CREDITO S/A                   00000000006951711                              CB35700002PENDENCIA FINANCEIRA        201507201507R$ 000002075CLARO               FLA 01000002889                  CB35700002REFIN                       201509201510R$ 000052252SANTANDER FINANCIAMEFLA 04000052558                  CB35900008CHEQUES S/FUNDOS-ACHEI CCF  201509201512   000000000ITAU                FLA                              CB35801OUTRAS OPER V1819202285  20150731OONR$ 0000020750000000170593696 CLARO               FLA 00002     CEOO     CB35801OUTRAS OPER V1802019362  20150731OONR$ 0000008130000000130667500 CLARO               FLA 00002     CEOO     CB35804FINANCIAMENTI1842232483  20151027FINR$ 00005225200000020023257469SANTANDER FINANCIAMEFLA 000020000 CEFI     CB35804FINANCIAMENTI1820510404  20150916FINR$ 000000305103140162016     BANCO YAMAH             000020000   FI     CB36020151217CCF-BB000008R$ 000000000ITAU               O01188818670667FORTALEZA                CE00008I           CB352LIN TSAI RESTAURANTE LTDA ME            1321740800250CESITUACAO DO CNPJ EM 08/01/2016: ATIVA      201401201502CB001NAO CLIENTE                                  0068268637900000000000000000000000         693676683220150719   2C");
		
		Set<String> chaves_ = hashOut.keySet();
		TreeSet<String> chaves = new TreeSet<String>(chaves_);
		
		for (String chave : chaves) {
		    System.out.println(chave + "=" + hashOut.get(chave));
		}
		//System.out.println(hashOut);
		
		
		
		
	    } catch (LayoutException e) {
		e.printStackTrace();
	    }
	    
	    
	    
	    
	}
	
	
}
