package edu.pe.cibertec.infracciones;

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

        // ACT
        PagoResponseDTO resultado = pagoService.procesarPago(multaId);

        // ASSERT
        assertEquals(400.00, resultado.getMontoPagado());
        assertEquals(EstadoMulta.PAGADA, multa.getEstado());
    }
}