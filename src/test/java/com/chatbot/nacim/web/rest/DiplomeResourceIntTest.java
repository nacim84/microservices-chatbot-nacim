package com.chatbot.nacim.web.rest;

import com.chatbot.nacim.MicroserviceschatbotnacimApp;

import com.chatbot.nacim.domain.Diplome;
import com.chatbot.nacim.repository.DiplomeRepository;
import com.chatbot.nacim.service.DiplomeService;
import com.chatbot.nacim.service.dto.DiplomeDTO;
import com.chatbot.nacim.service.mapper.DiplomeMapper;
import com.chatbot.nacim.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.chatbot.nacim.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DiplomeResource REST controller.
 *
 * @see DiplomeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroserviceschatbotnacimApp.class)
public class DiplomeResourceIntTest {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_OBTAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OBTAINING_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DiplomeRepository diplomeRepository;


    @Autowired
    private DiplomeMapper diplomeMapper;
    

    @Autowired
    private DiplomeService diplomeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiplomeMockMvc;

    private Diplome diplome;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiplomeResource diplomeResource = new DiplomeResource(diplomeService);
        this.restDiplomeMockMvc = MockMvcBuilders.standaloneSetup(diplomeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createEntity(EntityManager em) {
        Diplome diplome = new Diplome()
            .intitule(DEFAULT_INTITULE)
            .obtainingDate(DEFAULT_OBTAINING_DATE);
        return diplome;
    }

    @Before
    public void initTest() {
        diplome = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiplome() throws Exception {
        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();

        // Create the Diplome
        DiplomeDTO diplomeDTO = diplomeMapper.toDto(diplome);
        restDiplomeMockMvc.perform(post("/api/diplomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diplomeDTO)))
            .andExpect(status().isCreated());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeCreate + 1);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testDiplome.getObtainingDate()).isEqualTo(DEFAULT_OBTAINING_DATE);
    }

    @Test
    @Transactional
    public void createDiplomeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();

        // Create the Diplome with an existing ID
        diplome.setId(1L);
        DiplomeDTO diplomeDTO = diplomeMapper.toDto(diplome);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiplomeMockMvc.perform(post("/api/diplomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diplomeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIntituleIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setIntitule(null);

        // Create the Diplome, which fails.
        DiplomeDTO diplomeDTO = diplomeMapper.toDto(diplome);

        restDiplomeMockMvc.perform(post("/api/diplomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diplomeDTO)))
            .andExpect(status().isBadRequest());

        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkObtainingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setObtainingDate(null);

        // Create the Diplome, which fails.
        DiplomeDTO diplomeDTO = diplomeMapper.toDto(diplome);

        restDiplomeMockMvc.perform(post("/api/diplomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diplomeDTO)))
            .andExpect(status().isBadRequest());

        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiplomes() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get all the diplomeList
        restDiplomeMockMvc.perform(get("/api/diplomes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diplome.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE.toString())))
            .andExpect(jsonPath("$.[*].obtainingDate").value(hasItem(DEFAULT_OBTAINING_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get the diplome
        restDiplomeMockMvc.perform(get("/api/diplomes/{id}", diplome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diplome.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE.toString()))
            .andExpect(jsonPath("$.obtainingDate").value(DEFAULT_OBTAINING_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDiplome() throws Exception {
        // Get the diplome
        restDiplomeMockMvc.perform(get("/api/diplomes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome
        Diplome updatedDiplome = diplomeRepository.findById(diplome.getId()).get();
        // Disconnect from session so that the updates on updatedDiplome are not directly saved in db
        em.detach(updatedDiplome);
        updatedDiplome
            .intitule(UPDATED_INTITULE)
            .obtainingDate(UPDATED_OBTAINING_DATE);
        DiplomeDTO diplomeDTO = diplomeMapper.toDto(updatedDiplome);

        restDiplomeMockMvc.perform(put("/api/diplomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diplomeDTO)))
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testDiplome.getObtainingDate()).isEqualTo(UPDATED_OBTAINING_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Create the Diplome
        DiplomeDTO diplomeDTO = diplomeMapper.toDto(diplome);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiplomeMockMvc.perform(put("/api/diplomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diplomeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeDelete = diplomeRepository.findAll().size();

        // Get the diplome
        restDiplomeMockMvc.perform(delete("/api/diplomes/{id}", diplome.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diplome.class);
        Diplome diplome1 = new Diplome();
        diplome1.setId(1L);
        Diplome diplome2 = new Diplome();
        diplome2.setId(diplome1.getId());
        assertThat(diplome1).isEqualTo(diplome2);
        diplome2.setId(2L);
        assertThat(diplome1).isNotEqualTo(diplome2);
        diplome1.setId(null);
        assertThat(diplome1).isNotEqualTo(diplome2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiplomeDTO.class);
        DiplomeDTO diplomeDTO1 = new DiplomeDTO();
        diplomeDTO1.setId(1L);
        DiplomeDTO diplomeDTO2 = new DiplomeDTO();
        assertThat(diplomeDTO1).isNotEqualTo(diplomeDTO2);
        diplomeDTO2.setId(diplomeDTO1.getId());
        assertThat(diplomeDTO1).isEqualTo(diplomeDTO2);
        diplomeDTO2.setId(2L);
        assertThat(diplomeDTO1).isNotEqualTo(diplomeDTO2);
        diplomeDTO1.setId(null);
        assertThat(diplomeDTO1).isNotEqualTo(diplomeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(diplomeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(diplomeMapper.fromId(null)).isNull();
    }
}
