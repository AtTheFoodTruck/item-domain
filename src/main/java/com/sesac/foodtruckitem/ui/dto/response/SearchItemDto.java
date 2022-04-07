package com.sesac.foodtruckitem.ui.dto.response;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Address;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Images;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchItemDto {
    private Long itemId;
    private String itemName;
    private Images images;
    private int itemPrice;

    public static SearchItemDto of(Item item) {
        SearchItemDto itemDto = new SearchItemDto();
        itemDto.itemId = item.getId();
        itemDto.itemName = item.getName();
        itemDto.images = item.getItemImg();
        itemDto.itemPrice = item.getPrice();

        return itemDto;
    }
}
