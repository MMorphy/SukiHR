package hr.petkovic.iehr.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TransactionTypeUtil {

	public List<String> getPrivateExpenses(){
		List<String> returnList = new ArrayList<String>();
		returnList.add("Auto");
		returnList.add("Taxy");
		returnList.add("Stan");
		returnList.add("Hrana Stan");
		returnList.add("Izlasci");
		returnList.add("Sport");
		returnList.add("Kredit");
		returnList.add("Vikendica");
		returnList.add("Doktor");
		returnList.add("Pokloni");
		returnList.add("Putovanja");
		returnList.add("Privatno - Ostalo");
		return returnList;
	}

	public List<String> getBusinessExpenses(){
		List<String> returnList = new ArrayList<String>();
		returnList.add("Zabava j.d.o.o.");
		returnList.add("IBS Idea d.o.o.");
		returnList.add("Jarebica lov d.o.o.");
		returnList.add("Kesh plaća S");
		returnList.add("Kesh M");
		returnList.add("Poslovno - Ostalo");
		return returnList;
	}

	public List<String> getOperativeExpenses(){
		List<String> returnList = new ArrayList<String>();
		returnList.add("Placa");
		returnList.add("Bonus na placu 5%");
		returnList.add("Bonus na placu 10%");
		returnList.add("Gorivo");
		returnList.add("Cestarina");
		returnList.add("Pice");
		returnList.add("Bonus Konobari");
		returnList.add("Rezervni dijelovi aparati");
		returnList.add("Novi aparati");
		returnList.add("Auto: servis, osiguranje, registracija");
		returnList.add("Telefon i bon");
		returnList.add("Odvjetnik");
		returnList.add("Operativno - Ostalo");
		returnList.add("RAZDUZENJE");
		return returnList;

	}
}
