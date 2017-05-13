/**
 * Created by vladimir_antin on 13.5.17..
 */

function toast_message(text,btn_ok,mdToast) {
    mdToast.show(
        mdToast.simple()
            .textContent(text)
            .action(btn_ok)
            .highlightAction(true)
    );
}