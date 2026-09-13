package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId){
        return itemService.addItem(itemDto, userId);
    }
}
/*
Вот основные сценарии, которые должно поддерживать приложение:
Добавление новой вещи. Будет происходить по эндпоинту POST /items.
На вход поступает объект ItemDto.
userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
Именно этот пользователь — владелец вещи.
Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.

Редактирование вещи. Эндпоинт PATCH /items/{itemId}. Изменить можно название, описание и статус доступа к аренде.
Редактировать вещь может только её владелец.

Просмотр информации о конкретной вещи по её идентификатору. Эндпоинт GET /items/{itemId}.
Информацию о вещи может просмотреть любой пользователь.

Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них. Эндпоинт GET /items.

Поиск вещи потенциальным арендатором.
Пользователь передаёт в строке запроса текст, и система ищет вещи, содержащие этот текст в названии или описании.
Происходит по эндпоинту /items/search?text={text}, в text передаётся текст для поиска.
Проверьте, что поиск возвращает только доступные для аренды вещи.

 */