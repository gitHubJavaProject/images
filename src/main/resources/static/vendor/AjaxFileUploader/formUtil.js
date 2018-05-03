/**
 * Form Utils , require jquery
 * Created by Zhouyang on 14-5-7.
 */

/**
 * Clear all input in form, set text input val to ''.
 * @param formId the form ID to clear
 */
function clearForm(formId) {
    $(':input', '#' + formId)
        .removeAttr('checked')
        .removeAttr('selected')
        .not(':button, :submit, :reset, :hidden, :radio, :checkbox')
        .val('');
}

/**
 * Get size of inputfile
 * @param inputFile dom object of file input
 * @returns the size of file or -1 when get exception
 */
function getFileSize(inputFile) {
    var files = inputFile.files;
    if (!files) {
        try {
            return inputFile.document.fileSize;
        } catch (ex) {
            return -1;
        }
    } else if (files.length > 0) {
        return files[0].size;
    } else {
        return 0;
    }
}
/**
 * Check file size
 * @param inputFile dom object of file input
 * @param maxSize the max size of file in KB
 * @returns validate result
 */
function validateFileSize(inputFile, maxSize) {
    if (inputFile == null || inputFile == undefined
        || $(inputFile).val() == "") {
        return false;
    }

    if (typeof(maxSize) != 'number') {
        console.log("error of input type");
        return false;
    }

    var fileSize = getFileSize(inputFile);

    if (fileSize == 0) {
        swal({title: "请选择上传文件", type: 'error'});
        $(inputFile).val("");
        return false;
    } else if (fileSize < 0) {
        swal({title: "您当前的浏览器不支持该操作，请使用其他浏览器", type: 'error'});
        $(inputFile).val("");
        return false;
    }
    if (fileSize > 1024 * maxSize) {
        var unitChar = "Kb";

        var tmpSize = maxSize / 1024;
        console.log("tmpSize" + tmpSize);
        if (tmpSize >= 1) {
            unitChar = "Mb";
            maxSize = tmpSize;
        }

        swal({title: "文件大小不能超过" + maxSize + unitChar + "，请您重新选择。", type: 'error'});
        $(inputFile).val("");
        return false;
    }
    return true;
}

/**
 * Check file type
 * @param inputFile dom object of file input
 * @param allowTypes allowed types with string ".jpg,.jpeg, .png'
 * @returns validate result
 */
function validateFileType(inputFile, allowTypes) {
    var file = $(inputFile).val();
    var filename = file.replace(/.*(\/|\\)/, "");
    var fileExt = (/[.]/.exec(filename)) ? /[^.]+$/.exec(filename.toLowerCase()) : '';

    if (allowTypes.indexOf('.' + fileExt) < 0) {
        swal({title: "支持文件格式为" + allowTypes + "，请您选择合适的文件类型。", type: 'error'});
        $(inputFile).val("");
        return false;
    }
    return true;
}

$(document).ready(function () {
    $('form').submit(function () {
        if (typeof jQuery.data(this, "disabledOnSubmit") == 'undefined') {
            jQuery.data(this, "disabledOnSubmit", {submited: true});
            $('input[type=submit], input[type=button]', this).each(function () {
                $(this).attr("disabled", "disabled");
            });
            return true;
        }
        else {
            return false;
        }
    });
});
