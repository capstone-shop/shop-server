package com.capstone.shop;

import com.capstone.shop.user.v1.controller.MerchandiseController;
import com.capstone.shop.user.v1.service.MerchandiseService;
import com.capstone.shop.entity.Merchandise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(MerchandiseController.class)
class MerchandiseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchandiseService merchandiseService;

    private Page<Merchandise> merchandisePage;
    private List<Merchandise> merchandiseList;

    @BeforeEach
    void setUp() {
        // Sample Merchandise entity
        Merchandise merchandise = new Merchandise(1L, "검정 롱패딩", 20000, "서울특별시", 0, Collections.emptyList());
        Merchandise[] testcase = new Merchandise[]{
                new Merchandise(1L, "검정 롱패딩", 20000, "서울특별시", 0, Collections.emptyList())
        };

        // Create a page with a single merchandise item
        merchandiseList = Collections.singletonList(merchandise);
        merchandisePage = new PageImpl<>(merchandiseList, PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "like")), 1);

        // Mocking the service response
        Mockito.when(merchandiseService.getMerchandise(any(String.class), any(PageRequest.class)))
                .thenReturn(merchandisePage);
    }

    @Test
    void testGetMerchandise() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/merchandise")
                        .param("page", "0")
                        .param("size", "20")
                        .param("search", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.merchandise[0].id").value(1L))
                .andExpect(jsonPath("$.merchandise[0].name").value("검정 롱패딩"))
                .andExpect(jsonPath("$.merchandise[0].price").value(20000))
                .andExpect(jsonPath("$.merchandise[0].location").value("서울특별시"))
                .andExpect(jsonPath("$.merchandise[0].like").value(0))
                .andExpect(jsonPath("$.merchandise[0].images").isArray())
                .andExpect(jsonPath("$.merchandise[0].images", hasSize(0)))
                .andExpect(jsonPath("$.pagination.page").value(0))
                .andExpect(jsonPath("$.pagination.totalPages").value(1))
                .andExpect(jsonPath("$.pagination.requestSize").value(20))
                .andExpect(jsonPath("$.pagination.responseSize").value(1))
                .andExpect(jsonPath("$.pagination.sort").value("like,desc"))
                .andExpect(jsonPath("$.pagination.search").isString())
                .andExpect(jsonPath("$.pagination.search").value(""))
                .andExpect(jsonPath("$.pagination.first").value(true))
                .andExpect(jsonPath("$.pagination.last").value(true))
                .andDo(print());
    }
}