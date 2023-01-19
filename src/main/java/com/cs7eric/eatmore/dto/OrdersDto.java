package com.cs7eric.eatmore.dto;

import com.cs7eric.eatmore.entity.OrderDetail;
import com.cs7eric.eatmore.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
