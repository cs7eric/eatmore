package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.entity.AddressBook;
import com.cs7eric.eatmore.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

@Service
public class AddressBookService extends ServiceImpl<AddressBookMapper, AddressBook> implements com.cs7eric.eatmore.service.AddressBookService {
}
