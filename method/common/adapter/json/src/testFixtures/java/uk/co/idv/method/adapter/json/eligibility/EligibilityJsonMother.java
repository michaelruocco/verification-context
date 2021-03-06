package uk.co.idv.method.adapter.json.eligibility;

import static uk.co.mruoc.file.content.ContentLoader.loadContentFromClasspath;

public interface EligibilityJsonMother {

    static String eligible() {
        return loadContentFromClasspath("eligibility/eligible.json");
    }

    static String ineligible() {
        return loadContentFromClasspath("eligibility/ineligible.json");
    }

}
