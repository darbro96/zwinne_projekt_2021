package pl.projekt.grupa4.controller;

import org.springframework.web.bind.annotation.*;
import pl.projekt.grupa4.model.Projekt;
import pl.projekt.grupa4.model.ProjektStudent;
import pl.projekt.grupa4.model.Student;
import pl.projekt.grupa4.model.Zadanie;
import pl.projekt.grupa4.service.ProjektService;
import pl.projekt.grupa4.service.StudentService;
import pl.projekt.grupa4.service.ZadanieService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ApiController {
    private StudentService studentService;
    private ZadanieService zadanieService;
    private ProjektService projektService;

    public ApiController(StudentService studentService, ZadanieService zadanieService, ProjektService projektService) {
        this.studentService = studentService;
        this.zadanieService = zadanieService;
        this.projektService = projektService;
    }

    @GetMapping("/projekty")
    public Flux<Projekt> getProjekty() {
        return projektService.getProjekty();
    }

    @GetMapping("/projekty/{id}")
    public Mono<Projekt> getProjekt(@PathVariable("id") int idProjekt) {
        return projektService.getProjekt(idProjekt);
    }

    //dodanie nowego projektu
    @PostMapping("/addProjekt")
    public Mono<Projekt> addProjekt(@RequestParam(value = "nazwa") String nazwa, @RequestParam(value = "opis") String opis, @RequestParam("oddanie") String oddanie) {
        Projekt projekt = new Projekt();
        projekt.setNazwa(nazwa);
        projekt.setOpis(opis);
        projekt.setDataOddania(stringToDate(oddanie));
        projekt.setDataczasUtworzenia(LocalDateTime.now());
        return projektService.createProjekt(projekt);
    }

    @PostMapping("/zad-do-proj/{idZadania}/{idProjektu}")
    public Mono<Integer> zadanieDoProjektu(@PathVariable("idZadania") int idZadania, @PathVariable("idProjektu") int idProjektu) {
        Zadanie zadanie = zadanieService.getZadanie(idZadania).toProcessor().block();
        zadanie.setProjektId(idProjektu);
        return zadanieService.updateZadanie(zadanie);
    }

    //dodanie studenta do projektu
    @PostMapping("/stud-do-proj/{idStudenta}/{idProjektu}")
    public Mono<ProjektStudent> studentDoProjektu(@PathVariable("idStudenta") int idStudent, @PathVariable("idProjektu") int idProjektu) {
        ProjektStudent projektStudent = new ProjektStudent();
        projektStudent.setProjektId(idProjektu);
        projektStudent.setStudentId(idStudent);
        return projektService.addStudentToProjekt(projektStudent);
    }

    //usunięcie projektu o danym id
    @DeleteMapping("/usunProjekt/{id}")
    public Mono<Integer> usunProjekt(@PathVariable("id") int projektId) {
        return projektService.deleteProjekt(projektId);
    }

    @GetMapping("/zadania")
    public Flux<Zadanie> zadania() {
        return zadanieService.getZadania();
    }

    @GetMapping("/zadanie/{id}")
    public Mono<Zadanie> zadanie(@PathVariable("id") int id) {
        return zadanieService.getZadanie(id);
    }

    @GetMapping("/studenci")
    public Flux<Student> wszyscyStudentci() {
        return studentService.getStudenci();
    }

    @GetMapping("/student")
    public Mono<Student> student(@RequestParam("id") int id) {
        return studentService.getStudent(id);
    }

    @PostMapping("/nowyStudent")
    public Mono<Student> nowyStudent(@RequestParam("imie") String imie, @RequestParam("nazwisko") String nazwisko, @RequestParam("nr_indeksu") String index, @RequestParam("email") String email, @RequestParam("stacjonarny") boolean stacjonarny) {
        Student student = new Student();
        student.setImie(imie);
        student.setNazwisko(nazwisko);
        student.setNrIndeksu(index);
        student.setEmail(email);
        student.setStacjonarny(stacjonarny);
        return studentService.createStudent(student);
    }

    @PostMapping("/aktualizujStudenta")
    public Mono<Integer> aktualizujStudenta(@RequestParam("id") int id, @RequestParam("imie") String imie, @RequestParam("nazwisko") String nazwisko, @RequestParam("nr_indeksu") String index, @RequestParam("email") String email, @RequestParam("stacjonarny") boolean stacjonarny) {
        Student student = new Student();
        student.setStudentId(id);
        student.setImie(imie);
        student.setNazwisko(nazwisko);
        student.setNrIndeksu(index);
        student.setEmail(email);
        student.setStacjonarny(stacjonarny);
        return studentService.updateStudent(student);
    }

    @GetMapping("usunStudenta/{id}")
    public Mono<Integer> usunStudenta(@PathVariable("id") int id) {
        return studentService.deleteStudent(id);
    }


    //lista zadań z projektu
    @GetMapping("/zadania-z-projektu/{id}")
    public Flux<Zadanie> zadaniaZProjektu(@PathVariable("id") int projektId) {
        return zadanieService.getZadaniaFromProjekt(projektId);
    }

    @GetMapping("/studencizprojektu/{idProjekt}")
    public Flux<ProjektStudent> studentsIdsFromProjekt(@PathVariable("idProjekt") int idProjekt) {
        return projektService.studentIdsFromProjekt(idProjekt);
    }

    //dodanie nowego zadania
    @PostMapping("/addzadanie")
    public Mono<Zadanie> addZadanie(@RequestParam("nazwa") String nazwa, @RequestParam("opis") String opis, @RequestParam("kolejnosc") int kolejnosc, @RequestParam("oddanie") String oddanie, @RequestParam("idProjektu") int projektId) {
        Zadanie zadanie = new Zadanie();
        zadanie.setProjektId(projektId);
        zadanie.setOpis(opis);
        zadanie.setNazwa(nazwa);
        zadanie.setKolejnosc(kolejnosc);
        zadanie.setDataczasOddania(stringToDateTime(oddanie));
        return zadanieService.createZadanie(zadanie);
    }

    @GetMapping("/projektystudenta/{id}")
    public Flux<ProjektStudent> projektyStudenta(@PathVariable("id") int idStudent) {
        return projektService.studentsProjects(idStudent);
    }

    //usunięcie zadania na podstawie id
    @DeleteMapping("/usunzadanie/{id}")
    public Mono<Integer> usunZadanie(@PathVariable("id") int idZadanie) {
        return zadanieService.deleteZadanie(idZadanie);
    }

    //zakończenie zadania
    @GetMapping("/zakoncz/{id}")
    public Mono<Integer> zakonczZadanie(@PathVariable("id") int idZadania) {
        Zadanie zadanie = zadanieService.getZadanie(idZadania).toProcessor().block();
        zadanie.setSkonczone(true);
        return zadanieService.updateZadanie(zadanie);
    }

    //nieukończenie zadania
    @GetMapping("/niezakonczone/{id}")
    public Mono<Integer> zadanieNieukonczone(@PathVariable("id") int idZadania) {
        Zadanie zadanie = zadanieService.getZadanie(idZadania).toProcessor().block();
        zadanie.setSkonczone(false);
        return zadanieService.updateZadanie(zadanie);
    }

    private static LocalDate stringToDate(String date) {
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(5, 7));
        int d = Integer.parseInt(date.substring(8, 10));
        return LocalDate.of(y, m, d);
    }

    private static LocalDateTime stringToDateTime(String date) {
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(5, 7));
        int d = Integer.parseInt(date.substring(8, 10));
        return LocalDateTime.of(y, m, d, 0, 0);
    }

    private static String dateToString(LocalDate date) {
        return date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
    }
}
