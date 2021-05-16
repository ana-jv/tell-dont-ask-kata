package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderApprovalUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() throws Exception {
        Order initialOrder = new CreatedOrder(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        useCase.run(request);
        final Order savedOrder = orderRepository.getSavedOrder();

        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }


    @Test
    public void rejectedExistingOrder() throws Exception {
        Order initialOrder = new CreatedOrder(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        useCase.run(request);
        final Order savedOrder = orderRepository.getSavedOrder();

        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test(expected = RejectedOrderCannotBeApprovedException.class)
    public void cannotApproveRejectedOrder() throws Exception {
        Order initialOrder = new RejectedOrder(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() throws Exception {
        Order initialOrder = new ApprovedOrder(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() throws Exception {
        Order initialOrder = new ShippedOrder(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeRejected() throws Exception {
        Order initialOrder =  new ShippedOrder(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }
}
