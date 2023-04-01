package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.entity.AddressBook;
import com.cs7eric.eatmore.mapper.AddressBookMapper;
import com.cs7eric.eatmore.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl
        extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {
}
