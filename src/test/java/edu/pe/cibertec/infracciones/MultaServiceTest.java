package edu.pe.cibertec.infracciones;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.service.impl.MultaServiceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MultaServiceTest {

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    @Test
    void actualizarEstados_multaPendienteVencida_debeCambiarAVencida() {


        Multa multa = new Multa();
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaVencimiento(LocalDate.now().minusDays(1));

        when(multaRepository.findByEstado(EstadoMulta.PENDIENTE))
                .thenReturn(List.of(multa));


        multaService.actualizarEstados();


        assertEquals(EstadoMulta.VENCIDA, multa.getEstado());
        verify(multaRepository, times(1)).save(multa);
    }
}