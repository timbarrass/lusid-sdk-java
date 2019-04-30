package com.finbourne.lusid.integration;

import com.finbourne.lusid.ApiClient;
import com.finbourne.lusid.ApiException;
import com.finbourne.lusid.api.InstrumentsApi;
import com.finbourne.lusid.api.SearchApi;
import com.finbourne.lusid.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Ignore
public class InstrumentMasterTests {

    private static final String FIGI_SCHEME = "Figi";
    private static final String CUSTOM_INTERNAL_SCHEME = "ClientInternal";
    private static final String ISIN_SCHEME = "Isin";
    private static final String SEDOL_SCHEME = "Sedol";

    private static final String ISIN_PROPERTY_KEY = "Instrument/default/Isin";
    private static final String SEDOL_PROPERTY_KEY = "Instrument/default/Sedol";
    private static final String FIGI_PROPERTY_KEY = "Instrument/default/Figi";

    private static InstrumentsApi instrumentsApi;
    private static SearchApi searchApi;

    @BeforeClass
    public static void setUp() throws Exception {

        ApiClient apiClient = new ApiClientBuilder("secrets.json").build();

        instrumentsApi = new InstrumentsApi(apiClient);
        searchApi = new SearchApi(apiClient);

        seedInstrumentMaster();
    }

    @AfterClass
    public static void tearDown() throws ApiException
    {
        List<String>    ids = Arrays.asList(
                "BBG000C6K6G9",
                "BBG000C04D57",
                "BBG000FV67Q4",
                "BBG000BF0KW3",
                "BBG000BF4KL1"
        );

        for (String id : ids) {
            instrumentsApi.deleteInstrument(FIGI_SCHEME, id);
        }
    }

    private static void seedInstrumentMaster() throws ApiException
    {
        UpsertInstrumentsResponse upsertInstrumentsResponse = instrumentsApi.upsertInstruments(Stream.of(new Object[][] {

                {"correlationId1", new InstrumentDefinition()
                        .name("VODAFONE GROUP PLC")

                        /*
                            Instruments are created with a set of identifiers
                            each under a different scheme
                         */
                        .identifiers(new HashMap<String, InstrumentIdValue>() {
                            {
                                put(FIGI_SCHEME, new InstrumentIdValue().value("BBG000C6K6G9"));
                                put(CUSTOM_INTERNAL_SCHEME, new InstrumentIdValue().value("INTERNAL_ID_1"));
                                put(ISIN_SCHEME, new InstrumentIdValue().value("GB00BH4HKS39"));
                                put(SEDOL_SCHEME, new InstrumentIdValue().value("BH4HKS3"));
                            }
                        })},


                {"correlationId2", new InstrumentDefinition()
                        .name("BARCLAYS PLC")
                        .identifiers(new HashMap<String, InstrumentIdValue>() {
                            {
                                put(FIGI_SCHEME, new InstrumentIdValue().value("BBG000C04D57"));
                                put(CUSTOM_INTERNAL_SCHEME, new InstrumentIdValue().value("INTERNAL_ID_2"));
                                put(ISIN_SCHEME, new InstrumentIdValue().value("GB0031348658"));
                                put(SEDOL_SCHEME, new InstrumentIdValue().value("3134865"));
                            }
                        })},


                {"correlationId3", new InstrumentDefinition()
                        .name("NATIONAL GRID PLC")
                        .identifiers(new HashMap<String, InstrumentIdValue>() {
                            {
                                put(FIGI_SCHEME, new InstrumentIdValue().value("BBG000FV67Q4"));
                                put(CUSTOM_INTERNAL_SCHEME, new InstrumentIdValue().value("INTERNAL_ID_3"));
                                put(ISIN_SCHEME, new InstrumentIdValue().value("GB00BDR05C01"));
                                put(SEDOL_SCHEME, new InstrumentIdValue().value("BDR05C0"));
                            }
                        })},


                {"correlationId4", new InstrumentDefinition()
                        .name("SAINSBURY (J) PLC")
                        .identifiers(new HashMap<String, InstrumentIdValue>() {
                            {
                                put(FIGI_SCHEME, new InstrumentIdValue().value("BBG000BF0KW3"));
                                put(CUSTOM_INTERNAL_SCHEME, new InstrumentIdValue().value("INTERNAL_ID_4"));
                                put(ISIN_SCHEME, new InstrumentIdValue().value("GB00B019KW72"));
                                put(SEDOL_SCHEME, new InstrumentIdValue().value("B019KW7"));
                            }
                        })},

                {"correlationId5", new InstrumentDefinition()
                        .name("TAYLOR WIMPEY PLC")
                        .identifiers(new HashMap<String, InstrumentIdValue>() {
                            {
                                put(FIGI_SCHEME, new InstrumentIdValue().value("BBG000BF4KL1"));
                                put(CUSTOM_INTERNAL_SCHEME, new InstrumentIdValue().value("INTERNAL_ID_5"));
                                put(ISIN_SCHEME, new InstrumentIdValue().value("GB0008782301"));
                                put(SEDOL_SCHEME, new InstrumentIdValue().value("0878230"));
                            }
                        })}

        }).collect(Collectors.toMap(data -> (String)data[0], data -> (InstrumentDefinition)data[1])));

        assertThat(upsertInstrumentsResponse.getValues().size(), is(equalTo(5)));
    }


    @Test
    public void lookup_instrument_by_unique_id() throws ApiException
    {
        /*
            Look up an instrument that already exists in the instrument master by a
            unique id, in this case an OpenFigi, and also return a list of aliases
         */

        GetInstrumentsResponse lookedUpInstruments = instrumentsApi.getInstruments(FIGI_SCHEME, Collections.singletonList("BBG000C6K6G9"),
                null, null, Arrays.asList(ISIN_PROPERTY_KEY, SEDOL_PROPERTY_KEY));

        assertThat(lookedUpInstruments.getValues(), hasKey("BBG000C6K6G9"));

        Instrument instrument = lookedUpInstruments.getValues().get("BBG000C6K6G9");

        assertThat(instrument.getName(), is(equalTo("VODAFONE GROUP PLC")));

        List<Property>  identifiers = instrument.getProperties();
        identifiers.sort(Comparator.comparing(Property::getKey));

        assertThat(identifiers.get(0).getKey(), equalTo(ISIN_PROPERTY_KEY));
        assertThat(identifiers.get(0).getValue(), equalTo("GB00BH4HKS39"));
        assertThat(identifiers.get(1).getKey(), equalTo(SEDOL_PROPERTY_KEY));
        assertThat(identifiers.get(1).getValue(), equalTo("BH4HKS3"));
    }

    @Test
    public void lookup_instrument_by_market_identifier() throws ApiException
    {
        /*
            Look up instruments that already exists in the instrument master by a
            list of market identifiers
         */

        List<InstrumentMatch> instrumentMatch = searchApi.instrumentsSearch(
            Arrays.asList(
                new InstrumentSearchProperty().key(ISIN_PROPERTY_KEY).value("GB00BH4HKS39"),
                new InstrumentSearchProperty().key(ISIN_PROPERTY_KEY).value("BBG000C04D57"),
                new InstrumentSearchProperty().key(ISIN_PROPERTY_KEY).value("GB00BDR05C01"),
                new InstrumentSearchProperty().key(SEDOL_PROPERTY_KEY).value("B019KW7"),
                new InstrumentSearchProperty().key(SEDOL_PROPERTY_KEY).value("0878230")),
            null,
            true);

    }

    @Test
    public void find_non_mastered_instrument_from_external_source() throws ApiException
    {
        /*
            Look up an instrument not currently in the instrument master (NATIONAL GRID PLC)
         */

        List<InstrumentMatch> instrumentMatch = searchApi.instrumentsSearch(
                Collections.singletonList(
                        new InstrumentSearchProperty().key(FIGI_PROPERTY_KEY).value("BBG000FV67Q4")),
            null,
            false);

        InstrumentDefinition    instrumentDefinition = instrumentMatch.get(0).getExternalInstruments().get(0);

        assertThat(instrumentDefinition.getIdentifiers().get(FIGI_SCHEME).getValue(), is(equalTo("BBG000FV67Q4")));

        /*
            Add the instrument to the instrument master.  The result of the match contains
            the required information to master the instrument e.g. name, identifier, market
            identifier alias etc.
         */

        instrumentsApi.upsertInstruments(Collections.singletonMap("correlationId", instrumentDefinition));

        /*
            Get the instrument from the instrument master
         */
        Instrument instrument = instrumentsApi.getInstrument(FIGI_SCHEME, "BBG000FV67Q4", null, null, null);

        assertThat(instrument.getIdentifiers().get(FIGI_SCHEME), is(equalTo("BBG000FV67Q4")));
        assertThat(instrument.getName(), is(equalTo("NATIONAL GRID PLC")));
    }
}
