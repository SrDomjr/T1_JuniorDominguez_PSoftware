package edu.pe.cibertec.infracciones;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.service.impl.InfractorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class InfractorServiceTest {



        @Mock
        private InfractorRepository infractorRepository;

        @InjectMocks
        private InfractorServiceImpl infractorService;

        @Test
        void verificarBloqueo_conDosVencidasYTresPagadas_noDebeBloquear() {

            // ARRANGE
            Long infractorId = 1L;
            when(infractorRepository.contarMultasVencidas(infractorId)).thenReturn(2);

            // ACT
            infractorService.verificarBloqueo(infractorId);

            // ASSERT
            verify(infractorRepository, never()).save(any());
        }
    }

