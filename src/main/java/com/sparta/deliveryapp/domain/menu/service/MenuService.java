package com.sparta.deliveryapp.domain.menu.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMenuEnum;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.store.service.StoreService;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final StoreService storeService;
    private final MenuRepository menuRepository;

    // 메뉴 생성
    @Transactional
    public ApiResponse<Void> createMenu (Long storeId, MenuRequest menuRequest) {
        Store store = storeService.findByStoreId(storeId);
        Menu menu = Menu.menuWithStore(menuRequest, store);
        menuRepository.save(menu);
        return ApiResponse.ofApiResponseEnum(ApiResponseMenuEnum.MENU_SAVE_SUCCESS);
    }
}
