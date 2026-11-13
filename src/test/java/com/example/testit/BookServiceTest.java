package com.example.testit;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import com.example.testit.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MailService mailService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        // Préparation d'objets de test réutilisables
        book = new Book();
        book.setId(1L);
        book.setStatus(Status.DISPONIBLE);

        member = new Member();
        member.setId(1L);
    }
    @Test
    public void emprunterUnLivreDisponible() {

        //given

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));
        when(memberRepository.findById(member.getId()))
                .thenReturn(Optional.of(member));

        //when

        Book livreEmprunte = bookService.borrowBook(book.getId(), member.getId());

        //then

        assertThat(livreEmprunte).isNotNull();
        assertThat(livreEmprunte.getStatus()).isEqualTo(Status.EMPRUNTE);
        assertThat(livreEmprunte.getBorrower()).isEqualTo(member);

        verify(bookRepository, times(1)).save(book);
        verify(mailService, times(1)).sendMail(member, "Livre Emprunté", "Vous avez emprunté le livre " + book.getTitle());

    }

}
