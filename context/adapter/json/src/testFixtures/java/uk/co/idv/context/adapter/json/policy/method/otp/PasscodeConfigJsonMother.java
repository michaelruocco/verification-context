package uk.co.idv.context.adapter.json.policy.method.otp;

import static uk.co.mruoc.file.content.ContentLoader.loadContentFromClasspath;

public interface PasscodeConfigJsonMother {

    static String build() {
        return loadContentFromClasspath("method/otp/passcode-config.json");
    }

}