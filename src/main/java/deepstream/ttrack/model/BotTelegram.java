package deepstream.ttrack.model;

import deepstream.ttrack.common.utils.WebUtils;
import deepstream.ttrack.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotTelegram extends TelegramLongPollingBot {

    @Value("${t-track.bot.botUsername}")
    private String botUsername;

    @Value("${t-track.bot.botToken}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
    }

    public void sendTextAddOder(Long who, Order order, Integer totalOrders, Integer totalProduct){
        WebUtils.getUsername();
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(
                        "Người tạo đơn :" + WebUtils.getUsername() + "\n" +
                        "Tổng đơn hàng: " + totalOrders.toString() + "\n" +
                        "Tổng sản phẩm: " + totalProduct.toString() + "\n" +
                        "\n" +
                        "Tên sản phẩm: " + order.getProduct() + "\n" +
                        "Tên khách hàng: " + order.getCustomer() + "\n" +
                        "Số lượng: " + order.getQuantity() + "\n" +
                        "Địa chỉ: " + order.getAddress() + "\n" +
                        "Số điện thoại: " + order.getPhoneNumber() + "\n" +
                        "Trạng thái: " + order.getStatus() + "\n" +
                        "Thời gian tạo: " + order.getCreateAt().toLocalDate()   .toString() + "\n" +
                        "Mã giảm giá: " + order.getDiscountCode()
                )
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTextUpdateOder(Long who, Order order, Integer totalOrders, Integer totalProduct) {
        WebUtils.getUsername();
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(
                        "Người cập nhật đơn hàng :" + WebUtils.getUsername() + "\n" +
                        "Tổng đơn hàng: " + totalOrders.toString() + "\n" +
                        "Tổng sản phẩm: " + totalProduct.toString() + "\n" +
                        "\n" +
                        "Tên sản phẩm: " + order.getProduct() + "\n" +
                        "Tên khách hàng: " + order.getCustomer() + "\n" +
                        "Số lượng: " + order.getQuantity() + "\n" +
                        "Địa chỉ: " + order.getAddress() + "\n" +
                        "Số điện thoại: " + order.getPhoneNumber() + "\n" +
                        "Trạng thái: " + order.getStatus() + "\n" +
                        "Thời gian tạo: " + order.getCreateAt().toLocalDate().toString() + "\n" +
                        "Mã giảm giá: " + order.getDiscountCode()
                )
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendTextAddOderMissCall(Long who, Order order){
        WebUtils.getUsername();
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(
                        "Người tạo đơn :" + WebUtils.getUsername() + "\n" +
                        "được tạo từ cuộc gọi nhỡ :" + order.getPhoneNumber() + "\n"
                )
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
