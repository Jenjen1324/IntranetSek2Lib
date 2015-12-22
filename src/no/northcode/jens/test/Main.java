package no.northcode.jens.test;

import java.time.LocalDate;
import java.util.List;

import no.northcode.jens.intranetsek2.Lesson;
import no.northcode.jens.intranetsek2.Login;
import no.northcode.jens.intranetsek2.News;
import no.northcode.jens.intranetsek2.School;


@SuppressWarnings("unused")
public class Main {
	
	
	public static void main(String[] args) throws Exception
	{
		List<School> schools = School.getSchoolList();
		for(School s : schools) {
			System.out.println(s.getName());
		}
		
		
		Login l = new Login("jens.vogler", "-", "kho");
		List<Lesson> lessons = Lesson.getLessonByDay(l, LocalDate.parse("2015-12-15"));
		for(Lesson les : lessons) {
			System.out.println(les.toString());
		}
	}
}
