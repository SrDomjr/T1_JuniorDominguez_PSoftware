package edu.pe.cibertec.infracciones;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import edu.pe.cibertec.infracciones.dto.PagoResponseDTO;
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Pago;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import edu.pe.cibertec.infracciones.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    void procesarPago_multaPendienteHoy_aplicaDescuento20() {
        // ARRANGE
        Long multaId = 1L;
        Multa multa = new Multa();
        multa.setMonto(500.00);
        multa.setFechaEmision(LocalDate.now());
        multa.setFechaVencimiento(LocalDate.now().plusDays(30));
        multa.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(multaId)).thenReturn(Optional.of(multa));
        when(pagoRepository.save(any())).thenAnswer(i -> i.getArgument(0));


        PagoResponseDTO resultado = pagoService.procesarPago(multaId);


        assertEquals(400.00, resultado.getMontoPagado());
        assertEquals(EstadoMulta.PAGADA, multa.getEstado());
    }



    @Test
    void procesarPago_multaVencida12Dias_aplicaRecargo15() {
        // ARRANGE
        Long multaId = 2L;
        Multa multa = new Multa();
        multa.setMonto(500.00);
        multa.setFechaEmision(LocalDate.now().minusDays(12));
        multa.setFechaVencimiento(LocalDate.now().minusDays(2));
        multa.setEstado(EstadoMulta.VENCIDA);

        when(multaRepository.findById(multaId)).thenReturn(Optional.of(multa));
        when(pagoRepository.save(any())).thenAnswer(i -> i.getArgument(0));


        pagoService.procesarPago(multaId);


        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository, times(1)).save(pagoCaptor.capture());

        Pago pagoCaptured = pagoCaptor.getValue();
        assertEquals(75.00, pagoCaptured.getRecargo());
        assertEquals(0.00, pagoCaptured.getDescuentoAplicado());
        assertEquals(575.00, pagoCaptured.getMontoPagado());
    }
}