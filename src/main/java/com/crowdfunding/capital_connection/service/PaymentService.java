/*package com.crowdfunding.capital_connection.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceCreateRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {


    @Value("${mercadopago.access.token}")
    private String accessToken;

    /**
     * Crea una preferencia de donación para una causa.
     *
     * @param causeName Nombre de la causa.
     * @param donationAmount Monto donado.
     * @param donationMessage Mensaje o breve descripción de la donación.
     * @return URL de Checkout de Mercado Pago.
     * @throws MPException Si ocurre algún error al crear la preferencia.
     */
   /* public String createDonationPreference(String causeName, BigDecimal donationAmount, String donationMessage) throws MPException {
        // Inicializa el SDK con el token (sandbox)
        MercadoPagoConfig.setAccessToken(accessToken);

        // Construye el ítem de la preferencia utilizando el builder del nuevo SDK
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Donación a " + causeName)
                .quantity(1)
                // Se asigna el monto donado; acepta BigDecimal directamente
                .unitPrice(donationAmount)
                .build();

        // Construye el request para crear la preferencia, configurando los backUrls, autoReturn y notificationUrl.
        PreferenceCreateRequest request = PreferenceCreateRequest.builder()
                .items(Collections.singletonList(item))
                .backUrls(Map.of(
                        "success", "http://localhost:4200/donacion-exitosa",
                        "failure", "http://localhost:4200/donacion-error",
                        "pending", "http://localhost:4200/donacion-pendiente"))
                .autoReturn("approved")
                .notificationUrl("http://localhost:8080/api/pagos/notification")
                .build();

        // Crea la preferencia utilizando PreferenceClient
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        // Retorna la URL de Checkout (init_point) para que el usuario complete la donación
        return preference.getInitPoint();
    }
}
*/