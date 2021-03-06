package com.springboot.libraryOrg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.libraryOrg.models.Books;
import com.springboot.libraryOrg.repository.BooksRepository;
import com.springboot.libraryOrg.services.BooksService;

@Controller
public class BooksController {

	@Autowired
	private BooksService booksService;
	
	@Autowired
	private BooksRepository booksRepository;
	
	// display list of books
	//@GetMapping("/")
	@RequestMapping(value="/", method = {RequestMethod.GET, RequestMethod.POST})
	public String viewHomePage(Model model, String title) { //added in title!
		return findPaginated(1, "title", "asc", model);		
	}
	
	//@GetMapping("/showNewBooksForm")
	@RequestMapping(value="/showNewBooksForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String showNewBooksForm(Model model) {
		// create model attribute to bind form data
		Books books = new Books();
		model.addAttribute("books", books);
		return "new_books";
	}
		
	@PostMapping("/saveBooks")
	public String saveBooks(@ModelAttribute("books") Books books) {
		// save books to database
		booksService.saveBooks(books);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		// get books from the service
		Books books = booksService.getBooksById(id);
		
		// set books as a model attribute to pre-populate the form
		model.addAttribute("books", books);
		return "update_books";
	}
	
	@GetMapping("/deleteBooks/{id}")
	public String deleteBooks(@PathVariable (value = "id") long id) {
		
		// call delete books method 
		this.booksService.deleteBooksById(id);
		return "redirect:/";
	}
	
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		int pageSize = 50;
		
		model.addAttribute("books", new Books());
		
		Page<Books> page = booksService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Books> listBooks = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listBooks", listBooks);
        
		return "index";
	}
	
    @GetMapping("/booksSearch")
    public String booksSearch(Model model) {
    	
        model.addAttribute("books", new Books());
        
    	String page = findPaginated(1, "title", "asc", model);
    	model.addAttribute("page", page);
    	
    	List<Books> listBooks = booksRepository.findAll();
    	model.addAttribute("listBooks", listBooks);

        return "booksSearch";
    }
    
    //for full-text search, you must alter table within MySQL and add index name "full_text_search_idx" with FULLTEXT as type
	
	@PostMapping("/booksSearch")
	public String booksSearch(@Param("keyword") String keyword, Model model) {
		
		List<Books> foundBooks = booksService.search(keyword);
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("foundBooks", foundBooks);
		
		return "booksSearch";
	}
}