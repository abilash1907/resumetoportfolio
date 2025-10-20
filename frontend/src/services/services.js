import axios from "axios";
const BASE_URL='http://localhost:8080/api';

const extractContent =async(file) => {
    try {
        console.log(file,"file")
        const formData = new FormData();
        formData.append("file", file);
        formData.append("userId", "12345");
        console.log(formData,"formData")
        const res = await fetch(`${BASE_URL}/extractContent`,{
            method:'POST',
            body:formData
        });
        let result=await res?.json();
        return result;
    } catch (error) {
        throw error
    }
}

export {
    extractContent
}