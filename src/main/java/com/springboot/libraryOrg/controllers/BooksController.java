package com.springboot.libraryOrg.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.libraryOrg.models.Books;
import com.springboot.libraryOrg.services.BooksService;

@Controller
@RequestMapping("/books")
public class BooksController {
	
	@Autowired
	private BooksService booksService;
	
	@RequestMapping("/getAll")
	public String getAll(Model model) {
		List<Books> books = booksService.getAll();
		model.addAttribute("books", books);
		return "books";
	}
	
	@RequestMapping("/getOne")
	@ResponseBody
	public Optional<Books> getOne(Integer Id) {
		return booksService.getOne(Id);
	}
	
	@PostMapping("/addNew")
	public String addNew(Books books) {
		booksService.addNew(books);
		return "redirect:/books/getAll";
	}
}