package uk.co.idv.context.adapter.json.eligibility;

import uk.co.mruoc.file.content.ContentLoader;

public interface EligibilityJsonMother {

    static String build() {
        return ContentLoader.loadContentFromClasspath("eligibility/eligibility.json");
    }

}
