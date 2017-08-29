package br.com.tools.acessos.serasa.test;

import java.util.HashMap;

import br.com.tools.acessos.util.Util;

public class TestCore {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, Object> hashIn = new HashMap<String, Object>();
		String serasaBack = "R108F761447390680000PAULO CEZAR VALENTINI                                           BRASIL      099020071023N000002";
/*		Date dtFundacao = new Date();


		// pegando a data da fundacao
		try {
			dtFundacao = UtilData.validaData(serasaBack, UtilData.yyyyMMddDATE);
			System.out.println("data:" + dtFundacao);
		} catch (Exception e) {
			System.out.println("capotou!");
		}

*/	
		System.out.println("int: "+Integer.parseInt("00956"));
		hashIn.put("CAP_VOTANTE", Double.parseDouble(Util.subString(serasaBack, 97, 4))/10 );// uma casa decimal
		System.out.println("double: " + hashIn.get("CAP_VOTANTE") );// uma casa decimal
	}

}
