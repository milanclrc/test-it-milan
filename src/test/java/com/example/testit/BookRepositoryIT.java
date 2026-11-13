package com.example.testit;

import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.web.service.registry.ImportHttpServices;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryIT {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByMemberAndStatusAvec2MembresEtPlusieursLivres() {
        //given
        Member m1 = memberRepository.save(new Member("Milan"));
        Member m2 = memberRepository.save(new Member("Ethan"));

        //book1 DISPONIBLE
        Book book1 = new Book("1", "1");
        book1.setStatus(Status.DISPONIBLE);
        book1 = bookRepository.save(book1);

        //book2 EMPRUNTE par m1
        Book book2 = new Book("2", "2");
        book2.setStatus(Status.EMPRUNTE);
        book2.setBorrower(m1);
        book2 = bookRepository.save(book2);

        //book3 EMPRUNTE par m2
        Book book3 = new Book("3", "3");
        book3.setStatus(Status.EMPRUNTE);
        book3.setBorrower(m2);
        book3 = bookRepository.save(book3);

        //when
        List<Book> result = bookRepository.findByMemberAndStatus(m1.getId(), Status.DISPONIBLE);

        //then
        assertThat(result)
                .hasSize(2)
                .contains(book1, book2)
                .doesNotContain(book3);
    }
}
