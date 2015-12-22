package no.northcode.jens.test;

import java.util.List;

import no.northcode.jens.intranetsek2.Login;
import no.northcode.jens.intranetsek2.News;
import no.northcode.jens.intranetsek2.School;


@SuppressWarnings("unused")
public class Main {
	
	
	public static void main(String[] args) throws Exception
	{
		Login l = new Login("jens.vogler", "-", "kho");
		List<News> news = l.fetchNews();
		for(News n : news) {
			System.out.println(n.text);
		}
		String result = l.postRequest("https://intranet.tam.ch/kho/timetable/ajax-get-timetable", "startDate=1450047600000&endDate=1450566000000&studentId%5B%5D=4014351&holidaysOnly=0");
		System.out.println(result);
		
	}
}
