package br.com.tools.acessos.serasa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import br.com.tools.acessos.util.Util;
import br.com.tools.acessos.util.UtilData;

/**
 * @brief Classe contendo os Registros disponibilizados prelo SERASA-PF/PJ
 * @author ccsilva
 *
 */
public class Registros {
	/**
	 * @brief Registro B001 - Identificacao
	 * @param serasaBack
	 * @param hashIn
	 * @param log
	 * @throws Exception 
	 */
	public void registroB001(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtNasc = new Date();
		Date dtAtual = new Date();
		// logging IN
		logger.info(">> registroB001()");
		// pega a data de nascimento e valida
		try {
			logger.debug("Validando DATANASC...");
			dtNasc = UtilData.stringToDate(Util.subString(serasaBack, 76, 8), "yyyyMMdd");
		} catch (Exception e) {
			//System.out.println(">> Erro ao Parsing da DATANASC");
			logger.debug("Erro ao Parsing da DATANASC '" + 
					Util.subString(serasaBack, 76, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// tratando do dominio do status do CPF
/*		String szAux = Util.subString(serasaBack, 102, 1);
		if (szAux.equals("0")) {
			szAux = "CPF nao confirmado";
		} else if (szAux.equals("2")) {
			szAux = "Ativo";
		} else if (szAux.equals("6")) {
			szAux = "Suspenso";
		} else if (szAux.equals("9")) {
			szAux = "Cancelado";
		} else if (szAux.equals("8")) {
			szAux = "Novo (Ativo)";
		}
*/		
		// validando a data atual
		try {
			logger.debug("Validando DTATUAL...");
			dtAtual = UtilData.stringToDate(Util.subString(serasaBack, 103, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro ao Parsing DTATUAL '" + 
					Util.subString(serasaBack, 103, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numGrafias = Util.getCounter(hashIn, "B001_QTDCPFGRAFIA");

		// coloca informacoes do B001 na hash
		try {
			hashIn.put("B001_GRAFIA_" + numGrafias, Util.subString(serasaBack, 5, 45).trim());
			hashIn.put("B001_CPF_" + numGrafias, Util.subString(serasaBack, 50, 11));
			hashIn.put("B001_RG_" + numGrafias, Util.subString(serasaBack, 61, 15));
			hashIn.put("B001_DATANASC_" + numGrafias, dtNasc);
			hashIn.put("B001_CODCIDADE_" + numGrafias, Util.subString(serasaBack, 84, 4));
			hashIn.put("B001_TITULAR_" + numGrafias, Util.subString(serasaBack, 88, 1));
			hashIn.put("B001_LINKN_" + numGrafias, Integer.parseInt(Util.subString(serasaBack, 93, 9)));
			hashIn.put("B001_SITUACAO_" + numGrafias, Util.subString(serasaBack, 102, 1));
			hashIn.put("B001_DTATUAL_" + numGrafias, dtAtual);
			hashIn.put("B001_INDICERRO_" + numGrafias, Util.subString(serasaBack, 112, 1));
		} catch (Exception e) {
			logger.debug("registroB001()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// logging OUT
		logger.debug("<< registroB001()");

	}

	/**
	 * @brief Registro B002 - Cadastro Sintetico - BX14
	 * 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB002(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtAtualiza = new Date();
		Date dtNasc = new Date();
		Date dtEmissao = new Date();

		logger.debug(">> registroB002()");
		// validando a data da ultima alteracao cadastral
		try {
			logger.debug("B002_DTATUALIZA");
			dtAtualiza = UtilData.stringToDate(Util.subString(serasaBack, 8, 8), "yyyyMMdd");
			assert dtAtualiza == null : "DTATUALIZA Invalida";
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B002_DTATUALIZA '" + 
					Util.subString(serasaBack, 8, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// validando a data nascimento do CPF
		try {
			logger.debug("B002_DATANASC");
			dtNasc = UtilData.stringToDate(Util.subString(serasaBack, 16, 8), "yyyyMMdd");
			assert dtNasc == null : "DATANASC Invalida";
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B002_DATANASC '" + 
					Util.subString(serasaBack, 16, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// validando data de emissao do RG
		try {
			logger.debug("B002_DATAEMISS");
			dtEmissao = UtilData.stringToDate(Util.subString(serasaBack, 105, 8), "yyyyMMdd");
			assert dtEmissao == null : "DATAEMISS Invalida";
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B002_DATAEMISS '" + 
					Util.subString(serasaBack, 105, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsultas = Util.getCounter(hashIn, "B002_NUMREGISTROS");
		// continua preenchendo a hash
		try {
			hashIn.put("B002_DATAEMISS_" + numConsultas, dtEmissao);
			hashIn.put("B002_UFEMISSOR_" + numConsultas, Util.subString(serasaBack, 113, 2));
			hashIn.put("B002_DTATUALIZA_" + numConsultas, dtAtualiza);
			hashIn.put("B002_DATANASC_" + numConsultas, dtNasc);
			hashIn.put("B002_NOMEMAE_" + numConsultas, Util.subString(serasaBack, 24, 45));
			hashIn.put("B002_SEXO_" + numConsultas, Util.subString(serasaBack, 69, 1));
			hashIn.put("B002_TIPODOC_" + numConsultas, Util.subString(serasaBack, 70, 15));
			hashIn.put("B002_NUMDOC_" + numConsultas, Util.subString(serasaBack, 85, 15));
			hashIn.put("B002_ORGAOEMISS_" + numConsultas, Util.subString(serasaBack, 100, 5));
		} catch (Exception e) {
			logger.debug("registroB002()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB002()");
	}

	/**
	 * Registro B003 - Cadastro Sintetico - BX14
	 * 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB003(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		
		logger.debug(">> registroB003()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsultas = Util.getCounter(hashIn, "B003_NUMREGISTROS");
		// preenchendo a hash
		try {
			hashIn.put("B003_ESTADOCIVIL_" + numConsultas, Util.subString(serasaBack, 5, 12));
			hashIn.put("B003_DEPEND_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 17, 2)));
			hashIn.put("B003_ESCOLAR_" + numConsultas, Util.subString(serasaBack, 19, 12));
			hashIn.put("B003_MUNNASC_" + numConsultas, Util.subString(serasaBack, 31, 25));
			hashIn.put("B003_UFNASC_" + numConsultas, Util.subString(serasaBack, 52, 2));
			hashIn.put("B003_CPFCONJUGUE_" + numConsultas, Util.subString(serasaBack, 58, 11));
			hashIn.put("B003_DDDRES_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 69, 4)));
			hashIn.put("B003_FONERES_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 73, 8)));
			hashIn.put("B003_DDDCOML_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 81, 4)));
			hashIn.put("B003_FONECOML_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 85, 8)));
			hashIn.put("B003_RAMAL_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 93, 4)));
			hashIn.put("B003_CELULAR_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 97, 8)));
			hashIn.put("B003_CODCLIENTE_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 105, 2)));
		} catch (Exception e) {
			logger.debug("registroB003()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroB003()");
	}

	/**
	 * @brief Registro B004 - Cadastro Sintetico - BX14
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB004(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		
		logger.debug(">> registroB004()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsultas = Util.getCounter(hashIn, "B004_NUMREGISTROS");
		// preenchendo a hash
		try {
			hashIn.put("B004_ENDERECO_" + numConsultas, Util.subString(serasaBack, 5, 45));
			hashIn.put("B004_LOGRADOURO_" + numConsultas, Util.subString(serasaBack, 5, 30));
			hashIn.put("B004_NUMERO_" + numConsultas, Util.subString(serasaBack, 35, 5));
			hashIn.put("B004_COMPLEMENTO_" + numConsultas, Util.subString(serasaBack, 40, 10));
			hashIn.put("B004_BAIRRO_" + numConsultas, Util.subString(serasaBack, 50, 20));
			hashIn.put("B004_CIDADE_" + numConsultas, Util.subString(serasaBack, 70, 25));
			hashIn.put("B004_UF_" + numConsultas, Util.subString(serasaBack, 95, 2));
			hashIn.put("B004_CEP_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 97, 8)));
			hashIn.put("B004_DESDE_" + numConsultas, Util.subString(serasaBack, 105, 6));
		} catch (Exception e) {
			logger.debug("registroB004()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
		}
		logger.debug("<< registroB004()");
	}
	
	/**
	 * @brief Registro B005 - Cadastro Sintetico - BX14
	 * 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB005(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		
		logger.debug(">> registroB005()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsultas = Util.getCounter(hashIn, "B005_NUMREGISTROS");
		// preenchendo a hash
		try {
			hashIn.put("B005_OCUPACAO_" + numConsultas, Util.subString(serasaBack, 5, 10));
			hashIn.put("B005_RENDA_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 15, 9)));
			hashIn.put("B005_CTSERIE_" + numConsultas, Util.subString(serasaBack, 24, 5));
			hashIn.put("B005_NUMCT_" + numConsultas, Util.subString(serasaBack, 29, 7));
			hashIn.put("B005_PARTIC_" + numConsultas, Integer.parseInt(Util.subString(serasaBack, 37, 3)));
			// reservado ao serasa
			//hashIn.put("B005_RENDAMED_" + numConsultas, Util.subString(serasaBack, 40, 9));
			//hashIn.put("B005_LIMITE1", Util.subString(serasaBack, 49, 9));
			//hashIn.put("B005_LIMITE98", Util.subString(serasaBack, 58, 9));
			//hashIn.put("B005_QTDEOBS", Util.subString(serasaBack, 67, 6));
			//hashIn.put("B005_TIPOTECNOLOG_" + numConsultas, Util.subString(serasaBack, 73, 4));
			//hashIn.put("B005_CANALVENDA_" + numConsultas, Util.subString(serasaBack, 77, 5));
			//hashIn.put("B005_VLRALUGUEL_" + numConsultas, Util.subString(serasaBack, 82, 9));
		} catch (Exception e) {
			logger.debug("registroB005()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroB005()");
	}
	
	/**
	 * @brief Registro B006 - Cadastro Sintetico - BX14
	 * 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB006(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		
		logger.debug(">> registroB006()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsultas = Util.getCounter(hashIn, "B006_NUMREGISTROS");
		// preenchendo a hash
		try {
			hashIn.put("B006_EMPRESA_" + numConsultas, Util.subString(serasaBack, 5, 40));
			hashIn.put("B006_DESDE_" + numConsultas, Util.subString(serasaBack, 45, 6));
			hashIn.put("B006_TIPOMOEDA_" + numConsultas, Util.subString(serasaBack, 51, 3));
			hashIn.put("B006_PROFISSAO_" + numConsultas, Util.subString(serasaBack, 54, 30));
			hashIn.put("B006_CARGO_" + numConsultas, Util.subString(serasaBack, 84, 20));
		} catch (Exception e) {
			logger.debug("registroB006()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
		}
		logger.debug("<< registroB006()");
	}
	
	public void registroB011(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		
	}
	
	/**
	 * @brief Registro B012 - Referencias bancarias - BX64
	 * 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB012(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException{
		
		logger.debug(">> registroB012()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRef = Util.getCounter(hashIn, "B012_NUMREFERENCIAS");
		// preenchendo a hash
		try {
			hashIn.put("B012_BANCO_" + numRef, Util.subString(serasaBack, 5, 20));
			hashIn.put("B012_AGENCIA_" + numRef, Integer.parseInt(Util.subString(serasaBack, 25, 4)));
			hashIn.put("B012_CONTA_" + numRef, Integer.parseInt(Util.subString(serasaBack, 29, 8)));
			hashIn.put("B012_DDD_" + numRef, Integer.parseInt(Util.subString(serasaBack, 37, 4)));
			hashIn.put("B012_FONE_" + numRef, Integer.parseInt(Util.subString(serasaBack, 41, 8)));
			hashIn.put("B012_RAMAL_" + numRef, Integer.parseInt(Util.subString(serasaBack, 49, 4)));
			hashIn.put("B012_DESDE_" + numRef, Util.subString(serasaBack, 53, 6));
		} catch (Exception e) {
			logger.debug("registroB012()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroB012()");
		
	}

	/**
	 * @brief Registro B013 - Referencias Cheque Especial - BX64
	 * 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB013(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException{

		logger.debug(">> registroB013()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRef = Util.getCounter(hashIn, "B013_NUMREFERENCIAS");
		// preenchendo a hash
		try {
			hashIn.put("B013_BANCO1_" + numRef, Util.subString(serasaBack, 5, 20));
			hashIn.put("B013_LIMITE1_" + numRef, Integer.parseInt(Util.subString(serasaBack, 25, 9)));
			hashIn.put("B013_BANCO2_" + numRef, Util.subString(serasaBack, 34, 20));
			hashIn.put("B013_LIMITE2_" + numRef, Integer.parseInt(Util.subString(serasaBack, 54, 9)));
		} catch (Exception e) {
			logger.debug("registroB013()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroB013()");
	}
	
	public void registroB014(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB014() *** NAO IMPLEMENTADO ***");
	}
	public void registroB015(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB015() *** NAO IMPLEMENTADO ***");
	}
	public void registroB021(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB021() *** NAO IMPLEMENTADO ***");
	}
	public void registroB022(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB022() *** NAO IMPLEMENTADO ***");
	}
	public void registroB031(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB031() *** NAO IMPLEMENTADO ***");
	}
	public void registroB032(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB032() *** NAO IMPLEMENTADO ***");
	}
	public void registroB041(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB041() *** NAO IMPLEMENTADO ***");
	}
	public void registroB042(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB042() *** NAO IMPLEMENTADO ***");
	}
	public void registroB043(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB043() *** NAO IMPLEMENTADO ***");
	}
	public void registroB051(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB051() *** NAO IMPLEMENTADO ***");
	}
	public void registroB052(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB052() *** NAO IMPLEMENTADO ***");
	}
	public void registroB053(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB053() *** NAO IMPLEMENTADO ***");
	}
	public void registroB054(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB054() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * @brief Registro B231 - Compromissos - Previsao Pagamento - BX32 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB231(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {

		logger.debug(">> registroB231()");

		// preenchendo a hash
		try {
			// O Put do Hash dos valores finais; verifica se jah existe para somatoria
			String numCompromissos = Util.getCounter(hashIn, "B231_NUMCOMPROMISSOS");

			hashIn.put("B231_MODALIDADE_" + numCompromissos, Util.subString(serasaBack, 5, 12));
			// dados mes anterior
			hashIn.put("B231_MESANT_" + numCompromissos, Util.subString(serasaBack, 17, 6));
			hashIn.put("B231_QTDPARCELASMESANT_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 23, 3)));
			hashIn.put("B231_VLRMESANT_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 26, 7)));
			// dados mes atual
			hashIn.put("B231_MESATUAL_" + numCompromissos, Util.subString(serasaBack, 33, 6));
			hashIn.put("B231_QTDPARCELASMESATUAL_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 39, 3)));
			hashIn.put("B231_VLRMESATUAL_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 42, 7)));
			// dados meses anteriores
			hashIn.put("B231_MES1_" + numCompromissos, Util.subString(serasaBack, 49, 6));
			hashIn.put("B231_QTDPARCELASMES1_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 55, 3)));
			hashIn.put("B231_VLRMES1_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 58, 7)));
			hashIn.put("B231_MES2_" + numCompromissos, Util.subString(serasaBack, 65, 6));
			hashIn.put("B231_QTDPARCELASMES2_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 71, 3)));
			hashIn.put("B231_VLRMES2_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 74, 7)));
			hashIn.put("B231_MES3_" + numCompromissos, Util.subString(serasaBack, 81, 6));
			hashIn.put("B231_QTDPARCELASMES3_", Integer.parseInt(Util.subString(serasaBack, 87, 3)));
			hashIn.put("B231_VLRMES3_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 90, 7)));
			hashIn.put("B231_MES4_" + numCompromissos, Util.subString(serasaBack, 103, 6));
			hashIn.put("B231_QTDPARCELASMES4_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 106, 3)));
			hashIn.put("B231_VLRMES4_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 113, 7)));
		} catch (Exception e) {
			logger.debug("registroB231()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
		}

		logger.debug("<< registroB231()");
		
	}

	/**
	 * @brief Registro B232 - Compromissos - BX34 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB232(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		Date dtContrato = new Date();
		Date dtPriParcela = new Date();
		Date dtUltParcela = new Date();
		//SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);

		logger.debug(">> registroB232()");

		// preenchendo a hash
		try {
			// O Put do Hash dos valores finais; verifica se jah existe para somatoria
			String numCompromissos = Util.getCounter(hashIn, "B232_NUMCOMPROMISSOS");

			hashIn.put("B232_CONTRATO_" + numCompromissos, Util.subString(serasaBack, 5, 17));
			hashIn.put("B232_TPMODALIDADE_" + numCompromissos, Util.subString(serasaBack, 22, 2));
			// validando data contrato
			try {
				logger.debug("B232_DTCONTRATO");
				dtContrato = UtilData.stringToDate(Util.subString(serasaBack, 24, 8), "yyyyMMdd");
				assert dtContrato == null : "B232_DTCONTRATO Invalida";
			} catch (Exception e) {
				logger.debug("Erro ao Parsing B232_DTCONTRATO '" + 
						Util.subString(serasaBack, 24, 8) + "'. Atribuindo 01/01/1900 ");
			}
			// continua preenchendo a hash
			hashIn.put("B232_DTCONTRATO_" + numCompromissos, dtContrato);
			hashIn.put("B232_TPMOEDA_" + numCompromissos, Util.subString(serasaBack, 32, 3));
			hashIn.put("B232_VLRCONTRATO_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 35, 9)));
			hashIn.put("B232_VLRGARANTIA_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 44, 9)));
			hashIn.put("B232_VLRENTRADA_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 53, 9)));
			hashIn.put("B232_VLRPARCELA_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 62, 9)));
			// validando data 1a parcela
			try {
				logger.debug("B232_DTPRIPARCELA");
				dtPriParcela = UtilData.stringToDate(Util.subString(serasaBack, 71, 8), "yyyyMMdd");
				assert dtPriParcela == null : "B232_DTPRIPARCELA Invalida";
			} catch (Exception e) {
				logger.debug("Erro ao Parsing B232_DTPRIPARCELA '" + 
						Util.subString(serasaBack, 71, 8) + "'. Atribuindo 01/01/1900 ");
			}
			// continua preenchendo a hash
			hashIn.put("B232_DTPRIPARCELA_" + numCompromissos, dtPriParcela);
			// validando data ultima parcela
			try {
				logger.debug("B232_DTULTPARCELA");
				dtUltParcela = UtilData.stringToDate(Util.subString(serasaBack, 79, 8), "yyyyMMdd");
				assert dtUltParcela == null : "B232_DTULTPARCELA Invalida";
			} catch (Exception e) {
				logger.debug("Erro ao Parsing B232_DTULTPARCELA '" + 
						Util.subString(serasaBack, 71, 8) + "'. Atribuindo 01/01/1900 ");
			}
			// continua preenchendo a hash
			hashIn.put("B232_DTULTPARCELA_" + numCompromissos, dtUltParcela);
			hashIn.put("B232_QTDPARCELAS_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 87, 3)));
			hashIn.put("B232_NUMPRIPARCELA_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 90, 3)));
			hashIn.put("B232_PARCELACONT_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 93, 3)));
			hashIn.put("B232_DOCAVALISTA_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 96, 11)));
			hashIn.put("B232_COMPCB_" + numCompromissos, Util.subString(serasaBack, 107, 1));
			
		} catch (Exception e) {
			logger.debug("registroB232()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
		}

		logger.debug("<< registroB232()");
		
	}

	/**
	 * @brief Registro B233 - Compromissos - BX34 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB233(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {

			logger.debug(">> registroB233()");

			// preenchendo a hash
			try {
				// O Put do Hash dos valores finais; verifica se jah existe para somatoria
				String numCompromissos = Util.getCounter(hashIn, "B233_NUMCOMPROMISSOS");

				hashIn.put("B233_BEM_" + numCompromissos, Util.subString(serasaBack, 5, 60));
				hashIn.put("B233_PERIODPARC_" + numCompromissos, Util.subString(serasaBack, 65, 12));
				hashIn.put("B233_TIPOJURO_" + numCompromissos, Util.subString(serasaBack, 77, 1));
				hashIn.put("B233_TAXA_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 78, 8)));
				hashIn.put("B233_BONUS_" + numCompromissos, Integer.parseInt(Util.subString(serasaBack, 86, 3)));
			} catch (Exception e) {
				logger.debug("registroB233()" + e.getMessage());
				throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
			}

			logger.debug("<< registroB233()");
	}
	
	/**
	 * @brief Registro B234 - Detalhe de parcelas - BX34 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB234(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
			Date dt = new Date();
			//SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);

			logger.debug(">> registroB234()");

			// preenchendo a hash
			try {
				// O Put do Hash dos valores finais; verifica se jah existe para somatoria
				String numParcelas = Util.getCounter(hashIn, "B234_NUMPARCELAS");

				hashIn.put("B234_CONTRATO_" + numParcelas, Util.subString(serasaBack, 5, 17));
				hashIn.put("B234_NUMPARCELA_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 22, 3)));
				hashIn.put("B234_VLRPARCELA_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 25, 9)));
				// validando data vencimento parcela
				try {
					logger.debug("B234_DTVENCPARCELA");
					dt = UtilData.stringToDate(Util.subString(serasaBack, 34, 8), "yyyyMMdd");
					assert dt == null : "B234_DTVENCPARCELA Invalida";
				} catch (Exception e) {
					logger.debug("Erro ao Parsing B234_DTVENCPARCELA '" + 
							Util.subString(serasaBack, 34, 8) + "'. Atribuindo 01/01/1900 ");
				}
				// continua preenchendo a hash
				hashIn.put("B234_DTVENCPARCELA_" + numParcelas, dt);
				hashIn.put("B234_BANCO_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 42, 3)));
				hashIn.put("B234_AGENCIA_" + numParcelas, Util.subString(serasaBack, 45, 4));
				hashIn.put("B234_NUMCHEQUE1_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 49, 6)));
				hashIn.put("B234_OCORRENCIA_" + numParcelas, Util.subString(serasaBack, 55, 1));
				hashIn.put("B234_NUMPARCELAOCORR_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 56, 3)));
				hashIn.put("B234_VLRPARCELAOCORR_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 59, 9)));
				// validando data vencimento parcela
				try {
					logger.debug("B234_DTVENCPARCELA2");
					dt = UtilData.stringToDate(Util.subString(serasaBack, 68, 8), "yyyyMMdd");
					assert dt == null : "B234_DTVENCPARCELA2 Invalida";
				} catch (Exception e) {
					logger.debug("Erro ao Parsing B234_DTVENCPARCELA2 '" + 
							Util.subString(serasaBack, 68, 8) + "'. Atribuindo 01/01/1900 ");
				}
				// continua preenchendo a hash
				hashIn.put("B234_DTVENCPARCELA2_" + numParcelas, dt);
				hashIn.put("B234_BANCO2_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 76, 3)));
				hashIn.put("B234_AGENCIA2_" + numParcelas, Util.subString(serasaBack, 79, 4));
				hashIn.put("B234_NUMCHEQUE2_" + numParcelas, Integer.parseInt(Util.subString(serasaBack, 83, 6)));
				hashIn.put("B234_OCORRENCIA2_" + numParcelas, Util.subString(serasaBack, 89, 1));
				hashIn.put("B234_BAIXA1_" + numParcelas, Util.subString(serasaBack, 91, 1));
				hashIn.put("B234_BAIXA2_" + numParcelas, Util.subString(serasaBack, 92, 1));
				
			} catch (Exception e) {
				logger.debug("registroB234()" + e.getMessage());
				throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
			}

			logger.debug("<< registroB234()");
	}
		
	/**
	 * Registro B35B - Pendencia Financeiras - PEFIN
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroB35B(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		
		logger.debug(">> registroB35B()");
		// preenchendo a hash
		try {

			hashIn.put("B35B_QTDEOCORR", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B35B_VLR_TOTAL", Integer.parseInt(Util.subString(serasaBack, 10, 13)));

		} catch (Exception e) {
			logger.debug("registroB35B() " + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB35B()");
	}
	/**
	 * Registro B36B - 
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroB36B(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB36B() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro B38M - Pendencia Financeiras - PEFIN
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroB38M(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB38M()");
		// preenchendo a hash
		try {

			hashIn.put("B38M_QTDEOCORR", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B38M_VLR_TOTAL", Integer.parseInt(Util.subString(serasaBack, 10, 13)));

		} catch (Exception e) {
			logger.debug("registroB38M() " + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB38M()");
	}
		
	/**
	 * @brief Registro B280 - Calculo de Scoring 
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB280(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		
		logger.debug(">> registroB280()");
		// preenchendo a hash
		try {
			hashIn.put("B280_DESCSCORE", Util.subString(serasaBack, 5, 4));
			hashIn.put("B280_PONTSCORE", Integer.parseInt(Util.subString(serasaBack, 9, 6)));
			hashIn.put("B280_RANGESCORE", Util.subString(serasaBack, 15, 6));
			hashIn.put("B280_TAXASCORE", Util.subString(serasaBack, 21, 5));
			hashIn.put("B280_MSGSCORE", Util.subString(serasaBack, 26, 49));
			hashIn.put("B280_CODMSGSCORE", Util.subString(serasaBack, 75, 6));
		} catch (Exception e) {
			logger.debug("registroB280()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB280()");
	}

	/**
	 * @brief Registro B352 - Participacao Societaria
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB352(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		
		logger.debug(">> registroB352()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numParticipacao = Util.getCounter(hashIn, "B352_NUMPARTICIP");
		// preenchendo a hash
		try {
			hashIn.put("B352_EMPRESA_" + numParticipacao, Util.subString(serasaBack, 5, 40));
			hashIn.put("B352_CNPJ_" + numParticipacao, Util.subString(serasaBack, 45, 8));
			String percParc = Util.subString(serasaBack, 53, 5);
			if (percParc.trim().equals("")){
				percParc = "0";
			}
			hashIn.put("B352_PERCPARTIC_" + numParticipacao, (Integer.parseInt(percParc)/10));
			hashIn.put("B352_ESTADO_" + numParticipacao, Util.subString(serasaBack, 58, 2));
			hashIn.put("B352_SITUACAO_" + numParticipacao, Util.subString(serasaBack, 60, 43));
			hashIn.put("B352_DTINIPARTIC_" + numParticipacao, Util.subString(serasaBack, 103, 6));
		} catch (Exception e) {
			logger.debug("registroB352()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB352()");
	}

	/**
	 * @brief Registro B353 - RESUMO - Registro de Consultas
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB353(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
			
		logger.debug(">> registroB353()");
		Date dtPos = new Date();
		Date dtFicad = new Date();

		// preenchendo a hash
		try {
			hashIn.put("B353_QTDETOTALCRED", Integer.parseInt(Util.subString(serasaBack, 5, 3)));
			hashIn.put("B353_DATATUALCRED", Util.subString(serasaBack, 8, 6));
			hashIn.put("B353_QTDEATUALCRED", Integer.parseInt(Util.subString(serasaBack, 14, 3)));
			hashIn.put("B353_QTDEMES1CRED", Integer.parseInt(Util.subString(serasaBack, 17, 3)));
			hashIn.put("B353_QTDEMES2CRED", Integer.parseInt(Util.subString(serasaBack, 20, 3)));
			hashIn.put("B353_QTDEMES3CRED", Integer.parseInt(Util.subString(serasaBack, 23, 3)));
			hashIn.put("B353_QTDETOTALCHEQ", Integer.parseInt(Util.subString(serasaBack, 26, 3)));
			hashIn.put("B353_QTDEATUALCHEQ", Integer.parseInt(Util.subString(serasaBack, 29, 3)));
			hashIn.put("B353_QTDEMES1CHEQ", Integer.parseInt(Util.subString(serasaBack, 32, 3)));
			hashIn.put("B353_QTDEMES2CHEQ", Integer.parseInt(Util.subString(serasaBack, 35, 3)));
			hashIn.put("B353_QTDEMES3CHEQ", Integer.parseInt(Util.subString(serasaBack, 38, 3)));
		} catch (Exception e) {
			logger.debug("registroB353()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
		}

		// validando data da Posicao
		try {
			logger.debug("B353_POSICAO");
			dtPos = UtilData.stringToDate(Util.subString(serasaBack, 41, 8), "yyyyMMdd");
			assert dtPos == null : "B353_POSICAO Invalida";
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B353_POSICAO '" + 
					Util.subString(serasaBack, 41, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// continua preenchendo a hash
		hashIn.put("B353_POSICAO", dtPos);

		// validando data da FICAD/PF
		try {
			logger.debug("B353_DATAFICAD");
			dtFicad = UtilData.stringToDate(Util.subString(serasaBack, 49, 8), "yyyyMMdd");
			assert dtFicad == null : "B353_DATAFICAD Invalida";
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B353_DATAFICAD '" + 
					Util.subString(serasaBack, 49, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// continua preenchendo a hash
		hashIn.put("B353_DATAFICAD", dtFicad);

		logger.debug("<< registroB353()");
	}
	
	/**
	 * @brief Registro B354 - Detalhe Registro da Consulta - BX16
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB354(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB354()");

		// tratamento e preenchimetno da hash
		try {
			logger.debug("B354_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B354_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "B354_NUMCONSULTA");
		
		// preenchendo a hash
		String regData = (String) formatterOut.format(dtOcorr);
		try {
			
			hashIn.put("B354_DTOCORR_" + numConsulta, dtOcorr);
			hashIn.put("B354_ORIGEM_" + numConsulta, Util.subString(serasaBack, 13, 40));
			hashIn.put("B354_MODALIDADE_" + numConsulta, Util.subString(serasaBack, 53, 12));
			hashIn.put("B354_TIPOMOEDA_" + numConsulta, Util.subString(serasaBack, 65, 3));
			hashIn.put("B354_VALOR_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 68, 9)));
		} catch (Exception e) {
			logger.debug("registroB354()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap");
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B354_MENORDTCONSULTA", "B354_MAIORDTCONSULTA", regData);
		
		logger.debug("<< registroB354()");
	}
	
	/**
	 * @brief Registro B355 - Contumacia de Sustacao - BX18
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB355(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException{
		// Inicializa o formato de data de entrada (fixo)
		Date dtOcorr = new Date();

		logger.debug(">> registroB355()");

		// tratando a data da ocorrencia
		try {
			logger.debug("B355_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B355_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numOcorrencia = Util.getCounter(hashIn, "B355_NUMOCORR");
		
		// preenchendo a hash
		try {
			hashIn.put("B355_DTOCORR_" + numOcorrencia, dtOcorr);
			hashIn.put("B355_ORIGEM_" + numOcorrencia, Util.subString(serasaBack, 13, 20));
			hashIn.put("B355_AGENCIA_" + numOcorrencia, Integer.parseInt(Util.subString(serasaBack, 33, 4)));
			hashIn.put("B355_CONTACORR_" + numOcorrencia, Integer.parseInt(Util.subString(serasaBack, 37, 8)));
			hashIn.put("B355_CHINICIAL_" + numOcorrencia, Integer.parseInt(Util.subString(serasaBack, 45, 6)));
			hashIn.put("B355_CHFINAL_" + numOcorrencia, Integer.parseInt(Util.subString(serasaBack, 51, 6)));
			hashIn.put("B355_MOTIVOSUSTA_" + numOcorrencia, Util.subString(serasaBack, 57, 10));
			hashIn.put("B355_QTDEOCORR_" + numOcorrencia, Integer.parseInt(Util.subString(serasaBack, 67, 5)));
		} catch (Exception e) {
			logger.debug("registroB355()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroB355()");
	}
	
	/**
	 * @brief Registro B356 - Detalhe de Cheques Sustados - BX18 Usado para consulta de Pessoa Fisica e Juridica
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB356(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtInforme = new Date();

		logger.debug(">> registroB356()");
		// tratando a data da sustacao do cheque
		try {
			logger.debug("B356_INFORMEDT");
			dtInforme = UtilData.stringToDate(Util.subString(serasaBack, 59, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B356_INFORMEDT '" + 
					Util.subString(serasaBack, 59, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numSustados = Util.getCounter(hashIn, "B356_NUMCHEQUESSUSTADOS");
		
		// Hora HHMM (nao inclui o : exibe hhmm e nao hh:mm) feito isso para que se consiga testar menor e maior
		String regData = Util.subString(serasaBack, 67, 2).concat(Util.subString(serasaBack, 70, 2));
		// preenchendo a hash
		try {
			hashIn.put("B356_AGENCIA_" + numSustados, Integer.parseInt(Util.subString(serasaBack, 5, 4)));
			hashIn.put("B356_BANCO_" + numSustados, Util.subString(serasaBack, 9, 20));
			hashIn.put("B356_CONTACORR_" + numSustados, Integer.parseInt(Util.subString(serasaBack, 29, 8)));
			hashIn.put("B356_CHINICIAL_" + numSustados, Integer.parseInt(Util.subString(serasaBack, 37, 6)));
			hashIn.put("B356_CHFINAL_" + numSustados, Integer.parseInt(Util.subString(serasaBack, 43, 6)));
			hashIn.put("B356_MOTIVOSUSTA_" + numSustados, Util.subString(serasaBack, 49, 10));
			hashIn.put("B356_INFORMEDT_" + numSustados, dtInforme);
			hashIn.put("B356_INFORMEHR_" + numSustados, Util.subString(serasaBack, 67, 5));
			hashIn.put("B356_FONTE_" + numSustados, Util.subString(serasaBack, 72, 4));
			hashIn.put("B356_MENSAGEM_" + numSustados, Integer.parseInt(Util.subString(serasaBack, 76, 1)));
			hashIn.put("B356_DOCUMENTO_" + numSustados, Integer.parseInt(Util.subString(serasaBack, 77, 14)));
		} catch (Exception e) {
			logger.debug("registroB356()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B356_MENORDTCHQSUSTADO", "B356_MAIORDTCHQSUSTADO", regData);
		
		logger.debug("<< registroB356()");
	}

	/**
	 * @brief Registro B357 - Resumo Pendencia de Pagamento - BX20
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB357(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroB357()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numPendencias = Util.getCounter(hashIn, "B357_NUMPENDENCIAS");

		// preenche a hash
		try {
			hashIn.put("B357_QTDETOTAL_" + numPendencias, Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B357_DESCRICAO_" + numPendencias, Util.subString(serasaBack, 10, 28));
			hashIn.put("B357_DATAMENOR_" + numPendencias, Util.subString(serasaBack, 38, 6));
			hashIn.put("B357_DATAMAIOR_" + numPendencias, Util.subString(serasaBack, 44, 6));
			hashIn.put("B357_TIPMOEDA_" + numPendencias, Util.subString(serasaBack, 50, 3));
			hashIn.put("B357_VLR_ULTIMA_" + numPendencias, Integer.parseInt(Util.subString(serasaBack, 53, 9)));
			hashIn.put("B357_ORIGEM_" + numPendencias, Util.subString(serasaBack, 62, 20));
			hashIn.put("B357_FILIAL_" + numPendencias, Util.subString(serasaBack, 82, 4));
			hashIn.put("B357_TIPOPEFIN_" + numPendencias, Util.subString(serasaBack, 86, 2));
			hashIn.put("B357_VALTOTAL_" + numPendencias, Util.subString(serasaBack, 88, 9));
		} catch (Exception e) {
			logger.debug("registroB357()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB357()");
	}

	/**
	 * @brief Registro B358 - Detalhe Pendencia de Pagamento - BP22
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB358(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB358()");
		logger.debug(">> registroB358 -- " + serasaBack);
		
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numPEFINs = Util.getCounter(hashIn, "B358_NUMPEFIN");

		// pegando a data da ocorencia do pefin
		try {
			logger.debug("B358_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 32, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro ao Parsing B358_DTOCORR '" + 
					Util.subString(serasaBack, 32, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// pegando string da data de ocorrencia
		String regData = (String) formatterOut.format(dtOcorr);
		// preenchendo a hash
		try {
			String sPefin = Util.subString(serasaBack, 5, 2);
			if (sPefin.trim().equals("") || sPefin == null )
				sPefin = "00";
			hashIn.put("B358_DTOCORR_" + numPEFINs, dtOcorr);
			hashIn.put("B358_TIPOPEFIN_" + numPEFINs, sPefin);
			hashIn.put("B358_MODALIDADE_" + numPEFINs, Util.subString(serasaBack, 7, 12));
			hashIn.put("B358_CPF_PRINCIPAL_" + numPEFINs, Util.subString(serasaBack, 42, 1));
			hashIn.put("B358_SIGLAMODAL_" + numPEFINs, Util.subString(serasaBack, 40, 2));
			hashIn.put("B358_TPMOEDA_" + numPEFINs, Util.subString(serasaBack, 43, 3));
			hashIn.put("B358_VALOR_" + numPEFINs, Integer.parseInt(Util.subString(serasaBack, 46, 9)));
			hashIn.put("B358_ORIGEM_" + numPEFINs, Util.subString(serasaBack, 72, 20));
			hashIn.put("B358_FILIAL_" + numPEFINs, Util.subString(serasaBack, 92, 4));
			hashIn.put("B358_QUANTIDADE_" + numPEFINs, Integer.parseInt(Util.subString(serasaBack, 96, 5)));
		} catch (Exception e) {
			logger.debug("registroB358()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B358_MENORDTPENDPAG", "B358_MAIORDTPENDPAG", regData);

		logger.debug("<< registroB358()");
	}

	/**
	 * @brief Regsitro B359 - Resumo de Cheque sem Fundo - BX20
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB359(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroB359()");
		// preenchendo a hash
		try {
			hashIn.put("B359_QTDETOTAL", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B359_DESCRICAO", Util.subString(serasaBack, 10, 28));
			hashIn.put("B359_DATAMENOR", Util.subString(serasaBack, 38, 6));
			hashIn.put("B359_DATAMAIOR", Util.subString(serasaBack, 44, 6));
			hashIn.put("B359_TIPMOEDA", Util.subString(serasaBack, 50, 3));
			hashIn.put("B359_VLR_ULTIMA", Integer.parseInt(Util.subString(serasaBack, 53, 9)));
			hashIn.put("B359_ORIGEM", Util.subString(serasaBack, 62, 20));
			hashIn.put("B359_FILIAL", Util.subString(serasaBack, 82, 4));
		} catch (Exception e) {
			logger.debug("registroB359()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroB359()");
	}

	/**
	 * @brief Registro B360 - Detalhe de Cheque sem Fundos - BP24
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB360(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB360()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numChqSemFundos = Util.getCounter(hashIn, "B360_NUMCHQSEMFUNDOS");
		
		// pegando a data da ocorrencia dos cheques
		try {
			logger.debug("B360_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B360_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// pegando string da data de ocorrencia
		String regData = (String) formatterOut.format(dtOcorr);

		try {
			hashIn.put("B360_DTOCORR_" + numChqSemFundos, dtOcorr);
			hashIn.put("B360_NUMCHEQUE_" + numChqSemFundos, Util.subString(serasaBack, 13, 6));
			hashIn.put("B360_ALINEA_" + numChqSemFundos, Integer.parseInt(Util.subString(serasaBack, 19, 2)));
			hashIn.put("B360_QTDE_" + numChqSemFundos, Integer.parseInt(Util.subString(serasaBack, 21, 4)));
			hashIn.put("B360_VALOR_" + numChqSemFundos, Integer.parseInt(Util.subString(serasaBack, 28, 9)));
			hashIn.put("B360_BANCO_" + numChqSemFundos, Util.subString(serasaBack, 37, 30));
			hashIn.put("B360_AGENCIA_" + numChqSemFundos, Util.subString(serasaBack, 67, 4));
			hashIn.put("B360_CONTA_" + numChqSemFundos, Integer.parseInt(Util.subString(serasaBack, 104, 9)));
		} catch (Exception e) {
			logger.debug("registroB360()" + e.getMessage());
			System.out.println("registroB360() " + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B360_MENORDTSEMFUNDOS", "B360_MAIORDTSEMFUNDOS", regData);
		
		logger.debug("<< registroB360()");
	}

	/**
	 * @brief Registro B361 - Resumo Ocorrencias de Protestos
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB361(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroB361()");
		// preenchendo a hash
		try {
			hashIn.put("B361_QTDETOTAL", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B361_DESCRICAO", Util.subString(serasaBack, 10, 28));
			hashIn.put("B361_DATAMENOR", Util.subString(serasaBack, 38, 6));
			hashIn.put("B361_DATAMAIOR", Util.subString(serasaBack, 44, 6));
			hashIn.put("B361_TIPMOEDA", Util.subString(serasaBack, 50, 3));
			hashIn.put("B361_VALOR", Integer.parseInt(Util.subString(serasaBack, 53, 9)));
			hashIn.put("B361_CIDADE", Util.subString(serasaBack, 62, 25));
			hashIn.put("B361_UF", Util.subString(serasaBack, 87, 2));
		} catch (Exception e) {
			logger.debug("registroB361()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroB361()");
	}
	
	/**
	 * @brief Registro B362 - Detalhe Protesto - BP26
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB362(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB362()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numProtestos = Util.getCounter(hashIn, "B362_NUMPROTESTOS");

		// pegando a data da ocorrencia
		try {
			logger.debug("B362_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B362_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// pegando string da data de ocorrencia
		String regData = (String) formatterOut.format(dtOcorr);

		try {
			hashIn.put("B362_DTOCORR_" + numProtestos, dtOcorr);
			hashIn.put("B362_VALOR_" + numProtestos, Integer.parseInt(Util.subString(serasaBack, 16, 9)));
			hashIn.put("B362_CARTORIO_" + numProtestos, Integer.parseInt(Util.subString(serasaBack, 25, 4)));
			hashIn.put("B362_CIDADE_" + numProtestos, Util.subString(serasaBack, 29, 25));
			hashIn.put("B362_UF_" + numProtestos, Util.subString(serasaBack, 54, 2));
		} catch (Exception e) {
			logger.debug("registroB362()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B362_MENORDTPROTESTO", "B362_MAIORDTPROTESTO", regData);
		
		logger.debug("<< registroB362()");
	}

	/**
	 * @brief Regsitro B363 - Resumo de Acoes Judiciais
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB363(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroB363()");
		// preenchendo a hash
		try {
			hashIn.put("B363_QTDETOTAL", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B363_DESCRICAO", Util.subString(serasaBack, 10, 28));
			hashIn.put("B363_DATAMENOR", Util.subString(serasaBack, 38, 6));
			hashIn.put("B363_DATAMAIOR", Util.subString(serasaBack, 44, 6));
			hashIn.put("B363_TIPMOEDA", Util.subString(serasaBack, 50, 3));
			hashIn.put("B363_VALOR", Integer.parseInt(Util.subString(serasaBack, 53, 9)));
			hashIn.put("B363_NATUREZA", Util.subString(serasaBack, 62, 20));
			hashIn.put("B363_UF", Util.subString(serasaBack, 82, 2));
		} catch (Exception e) {
			logger.debug("registroB363()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB363()");
	}
	
	/**
	 * @brief Registro B364 - Detalhe Acao Judicial - BP28
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB364(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB364()");
		// pegando a data da ocorrencia
		try {
			logger.debug("B354_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B364_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numAcaoJud = Util.getCounter(hashIn, "B364_NUMACAOJUD");

		// pegando string da data de ocorrencia
		String regData = (String) formatterOut.format(dtOcorr);

		try {
			hashIn.put("B364_DTOCORR_" + numAcaoJud, dtOcorr);
			hashIn.put("B364_NATUREZA_" + numAcaoJud, Util.subString(serasaBack, 13, 20));
			hashIn.put("B364_PRINCIPAL_" + numAcaoJud, Util.subString(serasaBack, 33, 1));
			hashIn.put("B364_VALOR_" + numAcaoJud, Integer.parseInt(Util.subString(serasaBack, 37, 9)));
			hashIn.put("B364_DISTRIBUID_" + numAcaoJud, Integer.parseInt(Util.subString(serasaBack, 46, 4)));
			hashIn.put("B364_CIDADE_" + numAcaoJud, Util.subString(serasaBack, 54, 25));
		} catch (Exception e) {
			logger.debug("registroB364()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B364_MENORDTACAOJUD", "B364_MAIORDTACAOJUD", regData);

		logger.debug("<< registroB364()");
	}
	
	/**
	 * @brief Registro B365 - Resumo Participacoes em Falencias - BP30
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB365(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroB365()");

		try {
			hashIn.put("B365_TOTALOCORR", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B365_DESCRICAO", Util.subString(serasaBack, 10, 28));
			hashIn.put("B365_DATAMENOR", Util.subString(serasaBack, 38, 6));
			hashIn.put("B365_DATAMAIOR", Util.subString(serasaBack, 44, 6));
			hashIn.put("B365_EMPRESA", Util.subString(serasaBack, 50, 40));
		} catch (Exception e) {
			logger.debug("registroB365()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB365()");
	}
	
	/**
	 * @brief Registro B366 - Detalhe Participacao em Falencias - BP30
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB366(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB366()");
		
		// data da participacao em falencias
		try {
			logger.debug("B366_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B366_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numFalencias = Util.getCounter(hashIn, "B366_NUMFALENCIAS");
		// pegando string da data de ocorrencia
		String regData = (String) formatterOut.format(dtOcorr);

		// Preenchendo HashTable de saida e caching do registro
		try {
			hashIn.put("B366_DTOCORR_" + numFalencias, dtOcorr);
			hashIn.put("B366_TIPOOCORR_" + numFalencias, Util.subString(serasaBack, 13, 10));
			hashIn.put("B366_CNPJ_" + numFalencias, Util.subString(serasaBack, 23, 14));
			hashIn.put("B366_EMPRESA_" + numFalencias, Util.subString(serasaBack, 37, 45));
		} catch (Exception e) {
			logger.debug("registroB366()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B366_MENORDTPARTFALENC", "B366_MAIORDTPARTFALENC", regData);

		logger.debug("<< registroB366()");
	}

	/**
	 * @brief Registro B367 - Resumo Convem Devedores - BX20
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB367(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroB367()");
		// preenchendo a hash
		try {
			hashIn.put("B367_QTDETOTAL", Integer.parseInt(Util.subString(serasaBack, 5, 5)));
			hashIn.put("B367_DESCRICAO", Util.subString(serasaBack, 10, 28));
			hashIn.put("B367_DATAMENOR", Util.subString(serasaBack, 38, 6));
			hashIn.put("B367_DATAMAIOR", Util.subString(serasaBack, 44, 6));
			hashIn.put("B367_TIPMOEDA", Util.subString(serasaBack, 50, 3));
			hashIn.put("B367_VLR_ULTIMA", Integer.parseInt(Util.subString(serasaBack, 33, 9)));
			hashIn.put("B367_ORIGEM", Util.subString(serasaBack, 42, 20));
			hashIn.put("B367_LOCAL", Util.subString(serasaBack, 62, 4));
		} catch (Exception e) {
			logger.debug("registroB367()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroB369()");
	}

	/**
	 * @brief Registro B368 - Detalhe Divida Vencida - BP23
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB368(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		String fmtOut = "yyyyMMdd";
		SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();
		
		logger.debug(">> registroB368()");

		// pega a data da ocorrencia
		try {
			logger.debug("B368_DTOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B368_DTOCORR '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numDividas = Util.getCounter(hashIn, "B368_NUMDIVIDAS");
		// pegando string da data de ocorrencia
		String regData = (String) formatterOut.format(dtOcorr);

		try {
			hashIn.put("B368_DTOCORR_" + numDividas, dtOcorr);
			hashIn.put("B368_MODALIDADE_" + numDividas, Util.subString(serasaBack, 13, 2));
			hashIn.put("B368_VALOR_" + numDividas, Integer.parseInt(Util.subString(serasaBack, 18, 9)));
			hashIn.put("B368_ORIGEM_" + numDividas, Util.subString(serasaBack, 44, 20));
		} catch (Exception e) {
			logger.debug("registroB368()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		// faz processo de somatoria das datas
		sumMinorGreaterDate(hashIn, "B368_MENORDTDIVIDA", "B368_MAIORDTDIVIDA", regData);

		logger.debug("<< registroB368()");
	}

	/**
	 * @brief Registro B369 - Registro de Enderecos e Telefones Alternativos
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB369(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtConsulta = new Date();
		logger.debug(">> registroB369()");
		
		try {
			// define o tipo de registro: 1 e 2
			String tipoRegistro = Util.subString(serasaBack, 5, 1);
			// a leitura de quais campos ler, depende do tipo de registro
			hashIn.put("B369_TIPODEREG", tipoRegistro);
			// verifica qual tipo de registro
			switch (Integer.parseInt(tipoRegistro)) {
			case 0:
				hashIn.put("B369_DDDFONE", Integer.parseInt(Util.subString(serasaBack, 6, 3)));
				hashIn.put("B369_NROFONE", Integer.parseInt(Util.subString(serasaBack, 9, 9)));
				hashIn.put("B369_MENSAGEM", Util.subString(serasaBack, 18, 50));
				hashIn.put("B369_ORIGEM", Util.subString(serasaBack, 68, 10));
				hashIn.put("B369_DTCONSULTA", Util.subString(serasaBack, 78, 8));
				break;

			case 1:
				hashIn.put("B369_DDDFONE", Integer.parseInt(Util.subString(serasaBack, 6, 3)));
				hashIn.put("B369_NROFONE", Integer.parseInt(Util.subString(serasaBack, 9, 9)));
				hashIn.put("B369_ENDERECO", Util.subString(serasaBack, 18, 70));
				hashIn.put("B369_BAIRRO", Util.subString(serasaBack, 88, 20));
				hashIn.put("B369_CEP", Integer.parseInt(Util.subString(serasaBack, 108, 8)));
				break;

			default: // supoe-se que seja 2
				// pega a data da ocorrencia
				try {
					logger.debug("B369_DTCONSULTA");
					dtConsulta = UtilData.stringToDate(Util.subString(serasaBack, 97, 8), "yyyyMMdd");
				} catch (Exception e) {
					logger.debug("Erro Parsing B369_DTCONSULTA '" + 
							Util.subString(serasaBack, 97, 8) + "'. Atribuindo 01/01/1900 ");
				}
				hashIn.put("B369_CIDADE", Util.subString(serasaBack, 6, 30));
				hashIn.put("B369_UF", Util.subString(serasaBack, 36, 2));
				hashIn.put("B369_NOME", Util.subString(serasaBack, 38, 50));
				hashIn.put("B369_NRTELANT", Util.subString(serasaBack, 88, 9));
				hashIn.put("B369_DTCONSULTA", dtConsulta);
				hashIn.put("B369_ORIGEM", Util.subString(serasaBack, 105, 10));
				break;
			}

		} catch (Exception e) {
			logger.debug("registroB369()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroB369()");
	}
	/**
	 * @brief Registro B370 - Registro de Enderecos e Telefones Alternativos
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB370(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtOcorr = new Date();
		logger.debug(">> registroB370()");
		
		try {
			// define o tipo de registro: 1 e 2
			String tipoRegistro = Util.subString(serasaBack, 5, 1);
			// a leitura de quais campos ler, depende do tipo de registro
			hashIn.put("B370_TIPODEREG", tipoRegistro);
			// verifica qual tipo de registro
			switch (Integer.parseInt(tipoRegistro)) {
			case 1:
				hashIn.put("B370_DDDFONE", Integer.parseInt(Util.subString(serasaBack, 6, 3)));
				hashIn.put("B370_NROFONE", Integer.parseInt(Util.subString(serasaBack, 9, 9)));
				hashIn.put("B370_ENDERECO", Util.subString(serasaBack, 18, 70));
				hashIn.put("B370_BAIRRO", Util.subString(serasaBack, 88, 20));
				hashIn.put("B370_CEP", Integer.parseInt(Util.subString(serasaBack, 108, 8)));
				break;

			default: // supoe-se que seja 2
				// pega a data da ocorrencia
				try {
					logger.debug("B370_DTATUALIZA");
					dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 88, 8), "yyyyMMdd");
				} catch (Exception e) {
					logger.debug("Erro Parsing B370_DTATUALIZA '" + 
							Util.subString(serasaBack, 88, 8) + "'. Atribuindo 01/01/1900 ");
				}
				hashIn.put("B370_CIDADE", Util.subString(serasaBack, 6, 30));
				hashIn.put("B370_UF", Util.subString(serasaBack, 36, 2));
				hashIn.put("B370_NOME", Util.subString(serasaBack, 38, 50));
				hashIn.put("B370_DTATUALIZA", dtOcorr);
				break;
			}

		} catch (Exception e) {
			logger.debug("registroB370()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroB370()");
	}

	/**
	 * @brief Registro B373 - Registro com Dados do Recheque Online
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB373(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtAbertura = new Date();

		logger.debug(">> registroB373()");

		// pegando a data do Recheque
		try {
			logger.debug(">> B373_DTABERTURA");
			dtAbertura = UtilData.stringToDate(Util.subString(serasaBack, 38, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B373_DTABERTURA '" + 
					Util.subString(serasaBack, 38, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// preenchendo a hash
		try {
			hashIn.put("B373_NOMECORRENT", Util.subString(serasaBack, 5, 33));
			hashIn.put("B373_DTABERTURA", dtAbertura);
			hashIn.put("B373_MENSAGEM", Util.subString(serasaBack, 46, 20));
			hashIn.put("B373_BCO", Integer.parseInt(Util.subString(serasaBack, 66, 3)));
			hashIn.put("B373_AGENCIA", Integer.parseInt(Util.subString(serasaBack, 69, 4)));
			hashIn.put("B373_CTACORRENTE", Integer.parseInt(Util.subString(serasaBack, 73, 8)));
			hashIn.put("B373_CHINICIAL", Integer.parseInt(Util.subString(serasaBack, 81, 6)));
			hashIn.put("B373_CHFINAL", Integer.parseInt(Util.subString(serasaBack, 87, 6)));
			hashIn.put("B373_MOTIVO", Util.subString(serasaBack, 93, 9));
		} catch (Exception e) {
			logger.debug("registroB373()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroB373()");
	}
	
	/**
	 * Registro B38A
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroB38A(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB38A() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro B38A
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroB38B(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB38B() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * @brief Registro B381 - Grafias
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroB381(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB381()");
		
/*		// pegando a situacao na Receita
		String sSituacaoReceita = Util.subString(serasaBack, 104, 1);
		// verificando situacao na Receita
		if (sSituacaoReceita.equals("2")) {
			sSituacaoReceita = "2 - Ativo";
		} else if (sSituacaoReceita.equals("6")) {
			sSituacaoReceita = "6 - Suspenso";
		} else if (sSituacaoReceita.equals("9")) {
			sSituacaoReceita = "9 - Cancelado";
		} else if (sSituacaoReceita.equals("0")) {
			sSituacaoReceita = "0 - Razao Social nao confirmada";
		}
*/		
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numSituacao = Util.getCounter(hashIn, "B381_NUMSITUACAO");

		try {
			hashIn.put("B381_GRAFIA_" + numSituacao, Util.subString(serasaBack, 5, 70));
			hashIn.put("B381_CNPJ_" + numSituacao, Util.subString(serasaBack, 75, 8));
			hashIn.put("B381_CODCIDADE_" + numSituacao, Util.subString(serasaBack, 91, 4));
			hashIn.put("B381_SITUACAORECEITA_" + numSituacao, Util.subString(serasaBack, 104, 1));
			hashIn.put("B381_INDICERRO_" + numSituacao, Util.subString(serasaBack, 105, 1));
		} catch (Exception e) {
			logger.debug("registroB381()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB381()");
	}
	
	/**
	 * Registro B383
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroB383(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroB383() *** NAO IMPLEMENTADO ***");
	}
	
	
	/**
	 * @brief Registro B389 - Detalhe - REFIN - PJ
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroB389(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		//String fmtOut = "yyyyMMdd";
		//SimpleDateFormat formatterOut = new SimpleDateFormat(fmtOut);
		Date dtOcorr = new Date();

		logger.debug(">> registroB389()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRefin = Util.getCounter(hashIn, "B389_NUMREFIN");

		// pegando a data do Recheque
		try {
			logger.debug(">> B389_DATAOCORR");
			dtOcorr = UtilData.stringToDate(Util.subString(serasaBack, 32, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing B389_DATAOCORR '" + 
					Util.subString(serasaBack, 32, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// preenchendo hash
		try {
			hashIn.put("B389_MODALIDADE_" + numRefin, Util.subString(serasaBack, 7, 12));
			hashIn.put("B389_DATAOCORR_" + numRefin, dtOcorr);
			hashIn.put("B389_TPMOEDA_" + numRefin, Util.subString(serasaBack, 43, 3));
			hashIn.put("B389_QUANTIDADE_" + numRefin, Integer.parseInt(Util.subString(serasaBack, 96, 5)));
			hashIn.put("B389_VALOR_" + numRefin, Integer.parseInt(Util.subString(serasaBack, 46, 9)));
			hashIn.put("B389_ORIGEM_" + numRefin, Util.subString(serasaBack, 72, 20));
			hashIn.put("B389_FILIAL_" + numRefin, Util.subString(serasaBack, 92, 4));
		} catch (Exception e) {
			logger.debug("registroB389()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroB389()");
	}

	/**
	 * @brief Registro F900 - Calculo de Limites e Titulos em Cobranï¿½a
	 * @param dados
	 * @param mensagem
	 * @param hash
	 */
	public void registroF900(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroF900() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * @brief Registro R010 - Relato Contabilizacao
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR010(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR010() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R10A
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR10A(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR10A() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R10J
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR10J(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR10J() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R011 - Relato principais fontes (atualizacao)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR011(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR011() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R012 - Relato relacionamento com fornecedores
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR012(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR012() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R013 - Relato relacionamento com fornecedores (por periodo)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR013(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR013() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R014 - ??
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR014(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR014() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * @brief Registro R102 - Relato Endereço
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR102(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug("<< registroR102()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R102_NUMOCORR");

		// detalhes participantes
		try {
			hashIn.put("R102_ENDERECO_" + numConsulta, Util.subString(serasaBack, 5, 60));
			hashIn.put("R102_FANTASIA_" + numConsulta, Util.subString(serasaBack, 65, 51));
		} catch (Exception e) {
			logger.debug("registroR102()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroR102()");
	}
	
	/**
	 * @brief Registro R103 - Relato Localização
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR103(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug("<< registroR103()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R103_NUMOCORR");

		// detalhes participantes
		try {
			hashIn.put("R103_CIDADE_" + numConsulta, Util.subString(serasaBack, 5, 30));
			hashIn.put("R103_UF_" + numConsulta, Util.subString(serasaBack, 35, 2));
			hashIn.put("R103_CEP_" + numConsulta, Util.subString(serasaBack, 37, 9));
			hashIn.put("R103_DDD_" + numConsulta, Util.subString(serasaBack, 46, 4));
			hashIn.put("R103_TELEFONE_" + numConsulta, Util.subString(serasaBack, 50, 9));
			hashIn.put("R103_FAX_" + numConsulta, Util.subString(serasaBack, 59, 9));
			hashIn.put("R103_COD_EMBR_" + numConsulta, Util.subString(serasaBack, 68, 4));
		} catch (Exception e) {
			logger.debug("registroR103()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroR103()");
	}

	/**
	 * @brief Registro R104 - Relato da Atividade
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroR104(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtFundacao = new Date();
		Date dtCNPJ = new Date();

		logger.debug(">> registroR104()");

		// pegando a data da fundacao
		try {
			logger.debug("R104_DATAFUNDACAO");
			dtFundacao = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R104_DATAFUNDACAO '" + Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// Pegando a data da fundacao
		//String sDataFundacao = (String) formatterOut.format(dt);

		//  Buscar a data CNPJ
		try {
			logger.debug("R104_DATACNPJ");
			dtCNPJ = UtilData.stringToDate(Util.subString(serasaBack, 13, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R104_DATACNPJ '" + Util.subString(serasaBack, 13, 8) + ". Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRelatAtividade = Util.getCounter(hashIn, "R104_NUMRELAT");

		try {
			hashIn.put("R104_DATAFUNDACAO_" + numRelatAtividade, dtFundacao );
			hashIn.put("R104_DATACNPJ_" + numRelatAtividade, dtCNPJ);
			hashIn.put("R104_RAMO_ATIV_" + numRelatAtividade, Util.subString(serasaBack, 21, 54));
			hashIn.put("R104_CODIGOSERASA_" + numRelatAtividade, Util.subString(serasaBack, 75, 7));
			hashIn.put("R104_QTD_EMPREG_" + numRelatAtividade, Integer.parseInt(Util.subString(serasaBack, 82, 5)));
			hashIn.put("R104_NUMEROFILIAIS_" + numRelatAtividade, Integer.parseInt(Util.subString(serasaBack, 93, 6)));
			hashIn.put("R104_COD_CNAE_" + numRelatAtividade, Util.subString(serasaBack, 99, 7));
		} catch (Exception e) {
			logger.debug("registroR104()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR104()");
	}
	
	/**
	 * Registro R105 - Relato Filiais
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR105(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug("<< registroR105()");
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R105_NUMOCORR");

		// detalhes participantes
		try {
			hashIn.put("R105_NOME_FILIAL_" + numConsulta, Util.subString(serasaBack, 5, 30));
			hashIn.put("R105_COD_EMBR_" + numConsulta, Util.subString(serasaBack, 35, 4));
		} catch (Exception e) {
			logger.debug("registroR105()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroR105()");
	}

	/**
	 * Registro R106 - Relato Princinpais Produtos
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR106(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR106() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R107 - Relato Controle Societario (Atualizacao e capital social)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR107(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		// Inicializa o formato de data de entrada (fixo)
		Date dtAtualizacao = new Date();

		logger.debug(">> registroR107()");

		// pegando a data da fundacao
		try {
			logger.debug("R107_DT_ULT_ATU");
			dtAtualizacao = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R107_DT_ULT_ATU '" + Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRelatAtividade = Util.getCounter(hashIn, "R107_NUMOCORR");

		try {
			hashIn.put("R107_DT_ULT_ATU_" + numRelatAtividade, dtAtualizacao );
			//Double valSocial = Double.parseDouble(Util.subString(serasaBack, 13, 13)) / 100;
			hashIn.put("R107_VAL_CAP_SOCIAL_" + numRelatAtividade, Util.subString(serasaBack, 13, 13));
			//Double valReal = Double.parseDouble(Util.subString(serasaBack, 26, 13)) / 100;
			hashIn.put("R107_VAL_CAP_REAL_" + numRelatAtividade, Util.subString(serasaBack, 26, 13));
			//Double valAut = Double.parseDouble(Util.subString(serasaBack, 39, 13)) / 100;
			hashIn.put("R107_VAL_CAP_AUT_" + numRelatAtividade, Util.subString(serasaBack, 39, 13));
			hashIn.put("R107_DESC_NAC_" + numRelatAtividade, Util.subString(serasaBack, 52, 12));
			hashIn.put("R107_DESC_ORIG_" + numRelatAtividade, Util.subString(serasaBack, 64, 12));
			hashIn.put("R107_DESC_NAT_" + numRelatAtividade, Util.subString(serasaBack, 76, 12));
			hashIn.put("R107_JUNTA_" + numRelatAtividade, Util.subString(serasaBack, 88, 1));
		} catch (Exception e) {
			logger.debug("registroR107()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR107()");
	}

	/**
	 * @brief Registro R108 - RELATO CONTROLE SOCIETARIO (Detalhes dos Socios)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroR108(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtSocio = new Date();

		logger.debug(">> registroR108()");

		// pega data de entrada do socio
		try {
			logger.debug("R108_DATAENTRADASOCIO" );
			dtSocio = UtilData.stringToDate(Util.subString(serasaBack, 101, 6), "yyyyMM");
		} catch (Exception e) {
			logger.debug("Erro Parsing R108_DATAENTRADASOCIO '" + 
					Util.subString(serasaBack, 101, 6) + "'. Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numSocios = Util.getCounter(hashIn, "R108_NUMSOCIOS");

		try {
			hashIn.put("R108_TIPO_PESSOA_" + numSocios, Util.subString(serasaBack, 5, 1));
			hashIn.put("R108_CPFSOCIO_" + numSocios, Util.subString(serasaBack, 6, 9) + 
					Util.subString(serasaBack, 15, 2));
			hashIn.put("R108_TPDOC_" + numSocios, Util.subString(serasaBack, 17, 4));
			hashIn.put("R108_NOMESOCIO_" + numSocios, Util.subString(serasaBack, 21, 64));
			hashIn.put("R108_NACIONALSOCIO_" + numSocios, Util.subString(serasaBack, 85, 12));
			hashIn.put("R108_PERC_CAP_" + numSocios, Double.parseDouble(Util.subString(serasaBack, 97, 4))/10 );// uma casa decimal
			hashIn.put("R108_DATAENTRADASOCIO_" + numSocios, dtSocio);
			// indica se socio tem restricao (S/N)
			hashIn.put("R108_INDRESTRSOCIO_" + numSocios, Util.subString(serasaBack, 109, 1));
			hashIn.put("R108_PERC_CAP_VOTANTE_" + numSocios, Double.parseDouble(Util.subString(serasaBack, 110, 4))/10 );// uma casa decimal
			hashIn.put("R108_COD_SITUACAO_" + numSocios, Util.subString(serasaBack, 114, 2));
		} catch (Exception e) {
			logger.debug("registroR108()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR108()");
	}
	
	/**
	 * Registro 109 - Relato quadro administrativo (atualizacao)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR109(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		// Inicializa o formato de data de entrada (fixo)
		Date dtAtualizacao = new Date();

		logger.debug(">> registroR109()");

		// pegando a data da fundacao
		try {
			logger.debug("R109_DT_ULT_ATU");
			dtAtualizacao = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R109_DT_ULT_ATU '" + Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRelatAtividade = Util.getCounter(hashIn, "R109_NUMOCORR");

		try {
			hashIn.put("R109_DT_ULT_ATU_" + numRelatAtividade, dtAtualizacao );
			hashIn.put("R109_JUNTA_" + numRelatAtividade, Util.subString(serasaBack, 13, 1));
		} catch (Exception e) {
			logger.debug("registroR109()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR109()");
	}
	
	/**
	 * Registro 110 - Realato quadro administrativo (cont...)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR110(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR110()");
		
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRestricao = Util.getCounter(hashIn, "R110_NUMOCORR");
		// montando a hash do registro
		try {
			hashIn.put("R110_TIPO_PESS_" + numRestricao, Util.subString(serasaBack, 5, 1));
			hashIn.put("R110_CPF_" + numRestricao, Util.subString(serasaBack, 6, 9) + Util.subString(serasaBack, 15, 2));
			hashIn.put("R110_NOME_ADM_" + numRestricao, Util.subString(serasaBack, 17, 58));
			hashIn.put("R110_CARGO_" + numRestricao, Util.subString(serasaBack, 75, 12));
			hashIn.put("R110_NACIONALIDADE_" + numRestricao, Util.subString(serasaBack, 87, 12));
		} catch (Exception e) {
			logger.debug("registroR110()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR110()");
	}
	
	/**
	 * @brief Registro R111 - Relato quadro administrativo (Detalhes cont.)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroR111(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtFimMandato = new Date();
		Date dtIniMandato = new Date();
		
		logger.debug(">> registroR111()");
		
		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRestricao = Util.getCounter(hashIn, "R111_NUMOCORR");

		// pegando a data ini e fim do mandato
		try {
			logger.debug("R111_DTINIMANDATO");
			if ( "999999".equals(Util.subString(serasaBack, 30, 6)) ) { //Indeterminado
			    dtIniMandato = UtilData.stringToDate("19000101", "yyyyMMdd");
			} else {
				dtIniMandato = UtilData.stringToDate(Util.subString(serasaBack, 30, 8), "yyyyMMdd");
			}
			logger.debug("R111_DTFIMMANDATO");
			if ( "999999".equals(Util.subString(serasaBack, 38, 6)) ) { //Indeterminado
			    dtFimMandato = UtilData.stringToDate("99991231", "yyyyMMdd");
			} else {
			    dtFimMandato = UtilData.stringToDate(Util.subString(serasaBack, 38, 8), "yyyyMMdd");
			}
		} catch (Exception e) {
			logger.debug("Erro Parsing R111_DTFIMMANDATO '" + 
					Util.subString(serasaBack, 38, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// para ter o CPF, somo tambem o digito
		try {
			hashIn.put("R111_CPF_" + numRestricao, Util.subString(serasaBack, 6, 9) + Util.subString(serasaBack, 15, 2));
			hashIn.put("R111_INDRESTRICAO_" + numRestricao, Util.subString(serasaBack, 46, 1));
			hashIn.put("R111_DTINIMANDATO_" + numRestricao, dtIniMandato );
			hashIn.put("R111_DTFIMMANDATO_" + numRestricao, dtFimMandato );
		} catch (Exception e) {
			logger.debug("registroR111()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR111()");
	}
	
	/**
	 * Registro R112 - Relato participacoes (atualizacao)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR112(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR112() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R113 - Relato participações (detalhes da participada)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR113(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR113() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R114 - Relato participaçoes (detalhe participante)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR114(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR114()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R114_NUMOCORR");

		// detalhes participantes
		try {
			hashIn.put("R114_TIPO_PESS_" + numConsulta, Util.subString(serasaBack, 5, 1));
			hashIn.put("R114_CPF_" + numConsulta, Util.subString(serasaBack, 6, 9) + Util.subString(serasaBack, 15, 2));
			hashIn.put("R114_NOM_PART_" + numConsulta, Util.subString(serasaBack, 17, 67));
		} catch (Exception e) {
			logger.debug("registroR114()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroR114()");
	}
	
	/**
	 * Registro R115 - Relato participaçoes (detalhe participante, cont.)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR115(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR115()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R115_NUMOCORR");

		// detalhes participantes
		try {
			hashIn.put("R115_TIPO_PESS_" + numConsulta, Util.subString(serasaBack, 5, 1));
			hashIn.put("R115_CPF_" + numConsulta, Util.subString(serasaBack, 6, 9) + Util.subString(serasaBack, 15, 2));
			hashIn.put("R115_TPDOC_" + numConsulta, Util.subString(serasaBack, 17, 4));
			hashIn.put("R115_VINCULO_" + numConsulta, Util.subString(serasaBack, 21, 9));
			hashIn.put("R115_COD_EMBR_" + numConsulta, Util.subString(serasaBack, 30, 4));
			hashIn.put("R115_MUN_COD_EMBR_" + numConsulta, Util.subString(serasaBack, 34, 30));
			hashIn.put("R115_UF_COD_EMBR_" + numConsulta, Util.subString(serasaBack, 64, 2));
			// 3 inteiros e duas casas decimais
			Double percPart = Double.parseDouble(Util.subString(serasaBack, 66, 5)) / 100;
			hashIn.put("R115_PERC_PART_" + numConsulta, percPart );
			hashIn.put("R115_IND_RESTR_" + numConsulta, Util.subString(serasaBack, 71, 1));
			hashIn.put("R115_COD_SITUACAO_" + numConsulta, Util.subString(serasaBack, 72, 2));
		} catch (Exception e) {
			logger.debug("registroR115()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroR115()");
	}

	/**
	 * Registro R116 - Relato  Nomes semelhantes (01.01.15)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR116(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR116() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R119 - Relato - Antecessora
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR119(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR119() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R198
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR198(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR198() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R200 - Relato principais fontes (data ultima atualizacao)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR200(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR200() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R201 - RELATO - relacionamento com fornecedores mais antigos
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR201(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR201() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * @brief Registro R202 - Relato Potencial de Negocios
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroR202(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		// Inicializa o formato de data de entrada (fixo)
		Date dtPotencial = new Date();

		logger.debug(">> registroR202()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R202_NUMOCORR");
		String vlrPotencial = Util.subString(serasaBack, 27, 13);
		String mediaPotencial = Util.subString(serasaBack, 40, 13);
		
		// pegando a data potencial do negocio
		try {
			logger.debug("R202_DTPOTENCIAL");
			dtPotencial = UtilData.stringToDate(Util.subString(serasaBack, 19, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R202_DTPOTENCIAL '" + 
					Util.subString(serasaBack, 19, 8) + "'. Atribuindo 01/01/1900 ");
		}
		// tenta preencher a hash
		try {
			hashIn.put("R202_DESCNEGOCIO_" + numConsulta, Util.subString(serasaBack, 5, 14));
			hashIn.put("R202_DTPOTENCIAL_" + numConsulta, dtPotencial);
			hashIn.put("R202_VLRPOTENCIAL_" + numConsulta, Integer.parseInt(vlrPotencial));
			hashIn.put("R202_MEDIAPOTENCIAL_" + numConsulta, Integer.parseInt(mediaPotencial));
		} catch (Exception e) {
			logger.debug("registroR202()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		// Buscando variáveis para facilitar leitura pela política do Intellector
		
		/*hashIn.put("RefNegDtMaiorAcumulo", null);
		hashIn.put("RefNegValMaiorAcumulo", null);
		hashIn.put("RefNegMedMaiorAcumulo", null);
		hashIn.put("RefNegDtMaiorFatura", null);
		hashIn.put("RefNegValMaiorFatura", null);
		hashIn.put("RefNegMedMaiorFatura", null);
		hashIn.put("RefNegDtUltCompra", null);
		hashIn.put("RefNegValUltCompra", null);
		hashIn.put("RefNegMedUltCompra", null);*/
		
		if (hashIn != null && hashIn.containsKey("R202_DESCNEGOCIO_" + numConsulta)) {
		    if (((String) hashIn.get("R202_DESCNEGOCIO_" + numConsulta)).trim().equalsIgnoreCase("MAIOR ACUMULO")) {
			hashIn.put("RefNegDtMaiorAcumulo", (Date) hashIn.get("R202_DTPOTENCIAL_" + numConsulta));
			hashIn.put("RefNegValMaiorAcumulo", Double.valueOf(vlrPotencial));
			hashIn.put("RefNegMedMaiorAcumulo", Double.valueOf(mediaPotencial));
		    } else if (((String) hashIn.get("R202_DESCNEGOCIO_" + numConsulta)).trim().equalsIgnoreCase("MAIOR FATURA")) {
			hashIn.put("RefNegDtMaiorFatura", (Date) hashIn.get("R202_DTPOTENCIAL_" + numConsulta));
			hashIn.put("RefNegValMaiorFatura", Double.valueOf(vlrPotencial));
			hashIn.put("RefNegMedMaiorFatura", Double.valueOf(mediaPotencial));
		    } else if (((String) hashIn.get("R202_DESCNEGOCIO_" + numConsulta)).trim().equalsIgnoreCase("ULTIMA COMPRA")) {
			hashIn.put("RefNegDtUltCompra", (Date) hashIn.get("R202_DTPOTENCIAL_" + numConsulta));
			hashIn.put("RefNegValUltCompra", Double.valueOf(vlrPotencial));
			hashIn.put("RefNegMedUltCompra", Double.valueOf(mediaPotencial));
		    }
		}

		logger.debug("<< registroR202()");
	}
	
	/**
	 * @brief Registro R203 - Relato Historico de Pagamento
	 * @param dados
	 * @param mensagem
	 * @param hash
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroR203(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		logger.debug(">> registroR203()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R203_NUMOCORR");

		// descricao do registro (PONTUAL, 8-15, 16-30, 31-60, +60, A VISTA)
		try {
			hashIn.put("R203_DESCREG_" + numConsulta, Util.subString(serasaBack, 5, 14));
			hashIn.put("R203_QTDPERIODO_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 19, 6)));
			hashIn.put("R203_PERPERIODO_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 25, 4)));
		} catch (Exception e) {
			logger.debug("registroR203()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}

		logger.debug("<< registroR203()");
	}
	
	/**
	 * Registro R205 - Relato perfil de pagamentos
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR205(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR205() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R206 - RELATO - evolucao de compromissos com fornecedores
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR206(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR206() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R209 - Potencial de negocios ANALITICO (individual)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR209(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR209() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R210
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR210(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR210() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R211
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR211(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR211() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R212
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR212(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR212() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R213
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR213(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR213() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * Registro R218
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR218(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR218() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro A900
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroA900(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroA900() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R219
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR219(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR219() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * Registro R301 - Relato Controle de Consultas - Passagem
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR301(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR301()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R301_NUMOCORR");

		try {
			hashIn.put("R301_ANO_CONSULTA_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 5, 2)));
			hashIn.put("R301_MES_CONSULTA_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 7, 2)));
			hashIn.put("R301_DESC_MES_" + numConsulta, Util.subString(serasaBack, 9, 3));
			hashIn.put("R301_QT_CONS_EMP_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 12, 3)));
			hashIn.put("R301_QT_CONS_FIN_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 15, 3)));
		} catch (Exception e) {
			logger.debug("registroR301()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
					
		logger.debug("<< registroR301()");
	}

	/**
	 * Registro R302 - Relato últimas consultas
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR302(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		Date dtRisk = new Date();

		logger.debug(">> registroR302()");
		// pegando a data da consulta
		try {
			logger.debug("R302_DT_CONSULTA");
			dtRisk = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R302_DTRISK '" + 
				Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConsulta = Util.getCounter(hashIn, "R302_NUMOCORR");

		// pegando o relato das ultimas consultas
		try {
			hashIn.put("R302_DT_CONSULTA_" + numConsulta, dtRisk); // data da consulta
			hashIn.put("R302_NOME_CONS_" + numConsulta, Util.subString(serasaBack, 13, 35)); // nome do consultante
			hashIn.put("R302_QTD_CONS_DIA_" + numConsulta, Integer.parseInt(Util.subString(serasaBack, 48, 4)));
			hashIn.put("R302_CNPJ_CONS_" + numConsulta, Util.subString(serasaBack, 52, 9));
			hashIn.put("R302_TIPO_CONS_" + numConsulta, Util.subString(serasaBack, 61, 1));
		} catch (Exception e) {
			logger.debug("registroR302()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		logger.debug("<< registroR302()");
	}

	

	/**
	 * Registro R303
	 * 
	 * @author vfpacheco
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR303(String serasaBack, HashMap<String, Object> hashIn, Logger logger) throws IndexOutOfBoundsException {
		logger.debug(">> registroR303()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numRef = Util.getCounter(hashIn, "R303_NUMREGISTROS");

		try {
			// frase de alerta
			hashIn.put("R303_FRASE_ALERTA_" + numRef, Util.subString(serasaBack, 5, 111));

		} catch (Exception e) {
			logger.debug("registroR303()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		logger.debug("<< registroR303()");
	}

	
	/**
	 * @brief Registro R401 - RELATO - RiskScoring (12 meses)
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 * @throws IndexOutOfBoundsException 
	 */
	public void registroR401(String serasaBack, HashMap<String, Object> hashIn, Logger logger ) 
		throws IndexOutOfBoundsException {
		Date dtRisk = new Date();

		logger.debug(">> registroR401()");
		// pegando a data potencial do negocio
		try {
			logger.debug("R401_DTRISK");
			dtRisk = UtilData.stringToDate(Util.subString(serasaBack, 5, 8), "yyyyMMdd");
		} catch (Exception e) {
			logger.debug("Erro Parsing R401_DTRISK '" + 
					Util.subString(serasaBack, 5, 8) + "'. Atribuindo 01/01/1900 ");
		}

		// pegando o fator risk scoring 12 meses
		try {
			hashIn.put("R401_DTRISK", dtRisk); // data do calculo
			hashIn.put("R401_HRRISK", Util.subString(serasaBack, 13, 8)); // hora do calculo
			hashIn.put("R401_FATORRISK", Integer.parseInt(Util.subString(serasaBack, 21, 4))); // fator Riskscoring
		} catch (Exception e) {
			logger.debug("registroR401()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		try {
		    //R401_FATORPRIN - ( 3 interios e uma decimal)
		    Double fatorPrin = Double.parseDouble( Util.subString(serasaBack, 25, 4)) / 10.0;
		    
		    hashIn.put("R401_FATORPRIN", fatorPrin); // fator PRINAD
		} catch (Exception e) {
		    hashIn.put("R401_FATORPRIN", Double.valueOf(0.0));
		}
		
		
		logger.debug("<< registroR401()");
	}
	
	/**
	 * Registro R410
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR410(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR410() *** NAO IMPLEMENTADO ***");
	}
	
	/**
	 * @brief Registro R411 - RELATO - Informações de participantes com Anotações
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR411(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
			logger.debug(">> registroR411()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConcentre = Util.getCounter(hashIn, "R411_NUMOCORR");

		try {
			hashIn.put("R411_NOM_PARTIC_" + numConcentre, Util.subString(serasaBack, 5, 60));
			hashIn.put("R411_DOC_PARTIC_" + numConcentre, Util.subString(serasaBack, 70, 10));
			hashIn.put("R411_TIP_PARTIC_" + numConcentre, Util.subString(serasaBack, 80, 1));
		} catch (Exception e) {
			logger.debug("registroR411()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
					
		logger.debug("<< registroR411()");
	}

	/**
	 * @brief Registro R412 - RELATO - Informacoes do Concentre - Resumo
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR412(String serasaBack, HashMap<String, Object> hashIn, Logger logger )
		throws IndexOutOfBoundsException {
			logger.debug(">> registroR412()");
			logger.debug(">> registroR412 -- " + serasaBack);

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numConcentre = Util.getCounter(hashIn, "R412_NUMOCORR");

		try {
			hashIn.put("R412_QTDOCORR_" + numConcentre, Integer.parseInt(Util.subString(serasaBack, 5, 9)));
			hashIn.put("R412_GRUPOOCORR_" + numConcentre, Util.subString(serasaBack, 14, 27));
			hashIn.put("R412_MESINICIAL_" + numConcentre, Integer.parseInt(Util.subString(serasaBack, 44, 4)));
			hashIn.put("R412_MESFINAL_" + numConcentre, Integer.parseInt(Util.subString(serasaBack, 51, 4)));
			// A variável sResumoValor_R412 deverá ser preenchida com total valor, se a discriminação for protesto.
			/*hashIn.put("R412_TOTALVALOR_" + numConcentre, 
					Integer.parseInt(Util.subString(serasaBack, 14, 27).trim().equals("PROTESTO") ?
					Util.subString(serasaBack, 95, 13) : Util.subString(serasaBack, 58, 13) ));*/ // se nao for protesto, pega o valor da ocorrência
			
			hashIn.put("R412_TOTALVALOR_" + numConcentre, Integer.parseInt(	Util.subString(serasaBack, 95, 13)));
			
			
		} catch (Exception e) {
			logger.debug("registroR412()" + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
		
		
		if (hashIn != null && hashIn.containsKey("R412_GRUPOOCORR_" + numConcentre)) {

		    if (((String) hashIn.get("R412_GRUPOOCORR_" + numConcentre)).trim().equalsIgnoreCase("DIVIDA VENCIDA")) {
			hashIn.put("R412_MESFINAL_DIV_VENCIDA", hashIn.get("R412_MESFINAL_" + numConcentre));
			hashIn.put("R412_MESINICIAL_DIV_VENCIDA", hashIn.get("R412_MESINICIAL_" + numConcentre));
			hashIn.put("R412_QTDOCORR_DIV_VENCIDA", hashIn.get("R412_QTDOCORR_" + numConcentre));
			hashIn.put("R412_TOTALVALOR_DIV_VENCIDA", hashIn.get("R412_TOTALVALOR_" + numConcentre));			
		    } else if (((String) hashIn.get("R412_GRUPOOCORR_" + numConcentre)).trim().equalsIgnoreCase("FALEN/RECUP/CONC")) {
			hashIn.put("R412_MESFINAL_FALENCIA", hashIn.get("R412_MESFINAL_" + numConcentre));
			hashIn.put("R412_MESINICIAL_FALENCIA", hashIn.get("R412_MESINICIAL_" + numConcentre));
			hashIn.put("R412_QTDOCORR_FALENCIA", hashIn.get("R412_QTDOCORR_" + numConcentre));
			hashIn.put("R412_TOTALVALOR_FALENCIA", hashIn.get("R412_TOTALVALOR_" + numConcentre));			
		    } else if (((String) hashIn.get("R412_GRUPOOCORR_" + numConcentre)).trim().equalsIgnoreCase("ACAO JUDICIAL")) {
			hashIn.put("R412_MESFINAL_ACAO_JUDICIAL", hashIn.get("R412_MESFINAL_" + numConcentre));
			hashIn.put("R412_MESINICIAL_ACAO_JUDICIAL", hashIn.get("R412_MESINICIAL_" + numConcentre));
			hashIn.put("R412_QTDOCORR_ACAO_JUDICIAL", hashIn.get("R412_QTDOCORR_" + numConcentre));
			hashIn.put("R412_TOTALVALOR_ACAO_JUDICIAL", hashIn.get("R412_TOTALVALOR_" + numConcentre));			
		    } else if (((String) hashIn.get("R412_GRUPOOCORR_" + numConcentre)).trim().equalsIgnoreCase("PROTESTO")) {
			hashIn.put("R412_MESFINAL_PROTESTO", hashIn.get("R412_MESFINAL_" + numConcentre));
			hashIn.put("R412_MESINICIAL_PROTESTO", hashIn.get("R412_MESINICIAL_" + numConcentre));
			hashIn.put("R412_QTDOCORR_PROTESTO", hashIn.get("R412_QTDOCORR_" + numConcentre));
			hashIn.put("R412_TOTALVALOR_PROTESTO", hashIn.get("R412_TOTALVALOR_" + numConcentre));
		    } else if (((String) hashIn.get("R412_GRUPOOCORR_" + numConcentre)).trim().equalsIgnoreCase("CHEQUE")) {
			
			hashIn.put("R412_MESFINAL_DIV_VENCIDA", hashIn.get("R412_MESFINAL_" + numConcentre));
			hashIn.put("R412_MESINICIAL_DIV_VENCIDA", hashIn.get("R412_MESINICIAL_" + numConcentre));
			hashIn.put("R412_QTDOCORR_DIV_VENCIDA", hashIn.get("R412_QTDOCORR_" + numConcentre));
			hashIn.put("R412_TOTALVALOR_DIV_VENCIDA", hashIn.get("R412_TOTALVALOR_" + numConcentre));
			
		    }
		}		

		logger.debug("<< registroR412()");
	}

	/**
	 * Registro R38C - RELATO Identificação
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR38C(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR38C()");

		// O Put do Hash dos valores finais; verifica se jah existe para somatoria
		String numOcorrencia = Util.getCounter(hashIn, "R38C_NUMOCORR");
	
		try {
			hashIn.put("R38C_TIPO_SOCIED_" + numOcorrencia, Util.subString(serasaBack, 5, 60));
			hashIn.put("R38C_INSC_ESTADUAL_" + numOcorrencia, Util.subString(serasaBack, 65, 15));
			hashIn.put("R38C_SEFAZ_" + numOcorrencia, Util.subString(serasaBack, 80, 2));
		} catch (Exception e) {
			logger.debug("registroR38C() " + e.getMessage());
			throw new IndexOutOfBoundsException("Erro preenchendo HashMap " + this.toString());
		}
				
	logger.debug("<< registroR38C()");
	}
	
	/**
	 * Registro R999
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroR999(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
		logger.debug(">> registroR999() *** NAO IMPLEMENTADO ***");
	}

	/**
	 * @brief Classe dummy para a reflexao, uma vez que pode vir o registro P006 junto
	 * com a string de retorno; para a reflexao, ocorreria excecao, pois o metodo nao existe
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroP006(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
	}

	/**
	 * @brief Classe dummy para a reflexao, uma vez que pode vir o registro P002 junto
	 * com a string de retorno; para a reflexao, ocorreria excecao, pois o metodo nao existe
	 * @param serasaBack
	 * @param hashIn
	 * @param logger
	 */
	public void registroP002(String serasaBack, HashMap<String, Object> hashIn, Logger logger ){
	}

	/**
	 * @brief Uma especie de somador de ocorrencias de datas, guardando 
	 * maior e menor dado uma chave para acessar a hashtable
	 * @param hashIn
	 * @param key
	 * @param regData
	 */
	public void sumMinorGreaterDate( HashMap<String, Object> hashIn, String keyMin, String KeyGreat, String regData){
		/**
		 * a ideia eh pegar o mes atual e o mes que veio no registro do SERASA
		 * e fazer uma comparacao, verificando se sao diferentes e contabilizar
		 * quantas ocorrencias por CPF tem no mes vigente e quantas outras
		 * ocorrencias em outros meses; teremos entao duas somatorias, uma
		 * para o mes atual e outra somatoria englobando os outros meses.
		 * Guardaremos tambem a data da primeira ocorrencia e a mais recente
		 */
		// Verifica e pega a menor e maior data
		String menorData = (String) hashIn.get(keyMin);
		if (menorData == null) // se nao existir, atualiza direto a data menor
			hashIn.put(keyMin, regData); // agora tem 1
		String maiorData = (String) hashIn.get(KeyGreat);
		if (maiorData == null) // se nao existir, atualiza direto a data maior
			hashIn.put(KeyGreat, regData ); // agora tem 1
		// para evitar exception na primeira entrada, pego os valores da hash
		menorData = (String) hashIn.get(keyMin);
		maiorData = (String) hashIn.get(KeyGreat);

		// verifico se a data do registro atual eh menor que a menor data que tenho
		// como o formato da data eh YYYYMMDD, posso comparar diretamente
		if ((Integer.valueOf(regData)) < (Integer.valueOf(menorData))) // ops! eh menor, atualizo hash
			hashIn.put(keyMin, regData);
			
		// verifico se a data do registro atual eh maior que a maior data que tenho
		if ((Integer.valueOf(regData)) > (Integer.valueOf(maiorData))) // ops! eh maior, atualizo hash
			hashIn.put(KeyGreat, regData);
	}
}
