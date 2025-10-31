package org.example.onlineshop.payment.payment;

import jakarta.transaction.Transactional;
import org.example.onlineshop.payment.dto.PaymentNotificationResponseDto;
import org.example.onlineshop.order.model.Order;
import org.example.onlineshop.payment.dto.PaymentSearchCriteria;
import org.example.onlineshop.payment.model.Payment;
import org.example.onlineshop.order.repository.OrderRepository;
import org.example.onlineshop.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Create a new payment request for an order.
     * @param orderId the order ID
     * @param amount the payment amount
     * @param method payment method (e.g., CREDIT_CARD, WALLET)
     * @return created Payment entity
     */
    @Transactional
    public Payment createPayment(Long orderId, BigDecimal amount, String method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));

        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Payment can only be created for orders in PENDING status");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Confirm a payment after successful transaction.
     * @param paymentId payment ID
     * @param transactionReference reference from payment gateway
     * @return updated Payment entity
     */
    @Transactional
    public Payment confirmPayment(Long paymentId, String transactionReference) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be confirmed");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionReference(transactionReference);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Update order status to PAID
        Order order = payment.getOrder();
        order.setStatus("PAID");
        orderRepository.save(order);

        return payment;
    }

    /**
     * Refund a payment.
     * @param paymentId payment ID
     * @param reason reason for refund
     * @return updated Payment entity
     */
    @Transactional
    public Payment refundPayment(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException("Only paid payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundReason(reason);
        payment.setRefundedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Update order status to REFUND
        Order order = payment.getOrder();
        order.setStatus("REFUNDED");
        orderRepository.save(order);

        return payment;
    }

    /**
     * Retrieve a payment by its ID.
     * @param paymentId payment ID
     * @return Payment entity
     */
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));
    }

    /**
     * Retrieve all payments for a given order.
     * @param orderId order ID
     * @return list of payments
     */
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    /**
     * Update payment status.
     * @param paymentId payment ID
     * @param status new status
     * @return updated Payment entity
     */
    @Transactional
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with id: " + paymentId));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    /**
     * Search payments by criteria (example method, implement as needed).
     * @param criteria search criteria object
     * @return list of payments matching criteria
     */
    public List<Payment> searchPayments(PaymentSearchCriteria criteria) {
        // Implement search logic based on criteria fields
        // For example, use Specification or QueryDSL
        throw new UnsupportedOperationException("Search not implemented yet");
    }

    /**
     * Handle payment notification from gateway (webhook).
     * @param notification notification data
     */
    @Transactional
    public void handlePaymentNotification(PaymentNotificationResponseDto notification) {
        // Parse notification, find payment, update status accordingly
        // This method depends on gateway API and notification format
        throw new UnsupportedOperationException("Notification handling not implemented yet");
    }

    /**
     * Calculate the total amount to be paid for an order.
     * @param orderId order ID
     * @return total amount as BigDecimal
     */
    public BigDecimal calculatePaymentAmount(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        // Calculate total price considering discounts, taxes, shipping, etc.
        return order.getTotalPrice();
    }
}
