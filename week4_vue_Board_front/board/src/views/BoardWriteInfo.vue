<template>
    <div class="container my-4">
        <h1 class="my-4">게시판 - 등록</h1>
        <div class="row justify-content-center">
            <div class="col-md-12 bg-light">
                <form @submit.prevent="submitForm" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="save">
                    <div class="form-group row border-bottom p-3">
                        <label for="category_id" class="col-sm-2 col-form-label d-flex align-items-center">카테고리:</label>
                        <div class="col-sm-8">
                            <select id="category_id" name="category_id" class="form-control" required
                                v-model="formData.categoryId">
                                <option v-for="category in categories" :value="category.categoryId"
                                    :key="category.categoryId">
                                    {{ category.categoryName }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group row border-bottom p-3">
                        <label for="writer" class="col-sm-2 col-form-label d-flex align-items-center">작성자:</label>
                        <div class="col-sm-8">
                            <input autofocus type="text" id="writer" name="writer" class="form-control" required
                                v-model="formData.writer">
                        </div>
                    </div>

                    <div class="form-group row border-bottom p-3">
                        <label for="password" class="col-sm-2 col-form-label d-flex align-items-center">비밀번호:</label>
                        <div class="col-sm-4">
                            <input type="password" id="password" name="password" placeholder="비밀번호" class="form-control"
                                required v-model="formData.password">
                        </div>
                        <div class="col-sm-4">
                            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="비밀번호 확인"
                                class="form-control" required v-model="formData.confirmPassword">
                        </div>
                    </div>

                    <div class="form-group row border-bottom p-3">
                        <label for="title" class="col-sm-2 col-form-label d-flex align-items-center">제목:</label>
                        <div class="col-sm-8">
                            <input type="text" id="title" name="title" class="form-control" required
                                v-model="formData.title">
                        </div>
                    </div>

                    <div class="form-group row border-bottom p-3">
                        <label for="content" class="col-sm-2 col-form-label d-flex align-items-center">내용:</label>
                        <div class="col-sm-8">
                            <textarea id="content" name="content" class="form-control" rows="6" required
                                v-model="formData.content"></textarea>
                        </div>
                    </div>

                    <div class="form-group row p-3">
                        <label for="attachment1" class="col-sm-2 col-form-label d-flex align-items-center">첨부파일:</label>
                        <div class="col-sm-8">
                            <input type="file" id="attachment1" name="attachment1" class="form-control-file mb-2" @change="handleFileChange($event, 'attachment1')">
            <input type="file" id="attachment2" name="attachment2" class="form-control-file mb-2" @change="handleFileChange($event, 'attachment2')">
            <input type="file" id="attachment3" name="attachment3" class="form-control-file mb-2" @change="handleFileChange($event, 'attachment3')">
                        </div>
                    </div>

                    <div class="row mt-3 justify-content-center">
                        <div class="col-md-6">
                            <router-link
                                :to="`list?page=${searchCondition.page}&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}`"
                                class="btn btn-secondary btn-block">취소</router-link>
                        </div>
                        <div class="col-md-6">
                            <button type="submit" class="btn btn-primary btn-block">저장</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import api from "../scripts/APICreate.js";

export default {
    data() {
        return {
            formData: {
                categoryId: '',
                writer: '',
                password: '',
                confirmPassword: '',
                title: '',
                content: '',
                attachment1: null,
                attachment2: null,
                attachment3: null,
            },
            categories: [], // Assign the categories data from the backend to this array
            searchCondition: {
                page: '',
                category: '',
                searchText: '',
                startDate: '',
                endDate: '',
            },
        };
    },
      mounted() {
        this.getBoardInfo();
    },

    methods: {
        getBoardInfo(){
            const url = `write?page=${this.searchCondition.page}&category=${this.searchCondition.category}&searchText=${this.searchCondition.searchText}&startDate=${this.searchCondition.startDate}&endDate=${this.searchCondition.endDate}`;
            api
                .get(url)
                .then(response => {
                    const data = response.data;
                    this.searchCondition.category = data.searchCondition.category;
                    this.searchCondition.page = data.searchCondition.page;
                    this.searchCondition.searchText = data.searchCondition.searchText;
                    this.searchCondition.startDate = data.searchCondition.startDate;
                    this.searchCondition.endDate = data.searchCondition.endDate;
                    this.categories = data.categories;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },
         handleFileChange(event, fieldName) {
            // Handle the file change event
            // Access the selected file using event.target.files[0]
            // Assign it to the corresponding property in formData

            const file = event.target.files[0];

            // Example: Assign the file to the attachment1 property
            this.formData[fieldName] = file;
        },
        submitForm() {
            // Form validation checks
            if (this.formData.writer.length < 3 || this.formData.writer.length >= 5) {
                alert('작성자는 3글자 이상 5글자 미만이어야 합니다.');
                return;
            }

            if (
                this.formData.password.length < 4 ||
                this.formData.password.length >= 16 ||
                !/[A-Za-z0-9_$@#%&*]+/.test(this.formData.password) ||
                this.formData.password !== this.formData.confirmPassword
            ) {
                alert('비밀번호는 4글자 이상 16글자 미만이어야 하며, 영문, 숫자, 특수문자를 포함해야 하며, 비밀번호 확인과 일치해야 합니다.');
                return;
            }

            if (this.formData.title.length < 4 || this.formData.title.length >= 100) {
                alert('제목은 4글자 이상 100글자 미만이어야 합니다.');
                return;
            }

            if (this.formData.content.length < 4 || this.formData.content.length >= 2000) {
                alert('내용은 4글자 이상 2000글자 미만이어야 합니다.');
                return;
            }

            // Handle the form submission and API request to save the data
        },
    },
};
</script>