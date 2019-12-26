		Vue.use(VueRouter);
		ELEMENT.locale(ELEMENT.lang.en);

		function qs(query = {}) {
			return Object.entries(query).map(q => q[1] ? q.join("=") : null).filter(Boolean).join("&");
		}
		function errorMessage(data) {
			return data ? 'Error: ' + data : 'Error happened.';
		}

        function successMessage(response) {
             return response.status === 200 ? 'Message sent.'  : response.data;
        }


		var hlxPatientForm = Vue.component("hlx-patient-form", {
			template: "#hlx-patient-form",
			data() {
				return {
					loading: false,
					preview:{
					    text: '',
					    loaded: false,
					    collapse: false
					},
					context: {
						patient: {},
						existingEvents: [],
						generateEventTypes: [],
						generateEventType: undefined,
						existingEventId: undefined,
						eventType: undefined,
						channelType: undefined,
						receiverUrl: null
					},
					rules: {
						generateEventType: [
							{ required: true, message: 'Please select event', trigger: 'change' }
						],
						existingEventId: [
							{ required: true, message: 'Please select event', trigger: 'change' }
						],
						eventType: [
							{ required: true, message: 'Please select event type', trigger: 'change' }
						]
					}
				};
			},
			mounted() {
				this.loading = true;
				axios.get("/current-context").then(response => {
					Object.assign(this.context, response.data, { receiverUrl });
				}).catch(error => {
					this.$message.error(errorMessage(error.response.data.message));
				}).finally(() => {
					this.loading = false;
				});
			},
			methods: {
				onEventChange() {
					this.showPreview();
				},
				onEventTypeChange() {
					this.showPreview();
				},
				showPreview() {
					if (this.context.eventType === "Generate event" && this.context.generateEventType ||
						this.context.eventType === "Existing event" && this.context.existingEventId) {
						this.query("/preview", response => {
							this.preview.text = response.data;
							this.preview.loaded = true;
						});
					} else {
						this.preview.text = '';
						this.preview.loaded = false;
						this.preview.collapse = false;
					}
				},
				query(url, handler) {
					this.loading = true;
					axios.post(url, this.preparedData())
						.then(handler)
						.catch(error => {
							this.$message.error(errorMessage(error.response.data.message));
						}).finally(() => {
						this.loading = false;
					});
				},
				submitForm() {
					this.$refs.patientForm.validate(valid => {
						if (!valid) {
							return false;
						} else {
							this.query("/send-alert", response => {
								this.$message.success(successMessage(response))
							});
						}
					})
				},
				preparedData() {
					let alertRequest = {
						patientId: this.context.patient.id,
						channelType: this.context.channelType,
						receiverUrl: this.context.receiverUrl
					};

					if (this.context.eventType === "Generate event") {
						alertRequest.generateEventType = this.context.generateEventType;
					} else if (this.context.eventType === "Existing event") {
						alertRequest.existingEventId = this.context.existingEventId;
					}

					return alertRequest;
				}
			}
		});

		var router = new VueRouter({
			mode: "history",
			routes: [{
				path: "*",
				component: hlxPatientForm
			}]
		});

		var app = new Vue({
			el: "#app",
			router
		});
