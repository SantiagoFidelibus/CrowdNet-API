/*package com.crowdfunding.capital_connection.controller;

import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import com.crowdfunding.capital_connection.controller.dto.MercadoPagoDonationRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/create-preference")
    public String createPreference(@RequestBody MercadoPagoDonationRequest mercadoPagoDonationRequest) {
        try {
            Preference preference = new Preference();

            // Crear un ítem que representa la donación
            Item item = new Item();
            item.setTitle(mercadoPagoDonationRequest.getDescription()); // Usar la descripción de la donación
            item.setQuantity(1);
            item.setUnitPrice(mercadoPagoDonationRequest.getAmount().doubleValue()); // Convertir BigDecimal a double

            preference.appendItem(item);

            // Configurar el pagador
            Payer payer = new Payer();
            payer.setEmail(mercadoPagoDonationRequest.getEmail()); // Usar el email del pagador
            preference.setPayer(payer);

            // Crear la preferencia
            Preference result = preference.save();

            return result.getInitPoint(); // URL de pago
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al crear la preferencia de pago";
        }
    }
}
*/