// eslint-disable-next-line import/no-extraneous-dependencies
const { Builder, By, until } = require('selenium-webdriver');
// eslint-disable-next-line import/no-extraneous-dependencies
const chrome = require('selenium-webdriver/chrome');
const path = require('path');

// eslint-disable-next-line no-undef
jest.setTimeout(30000); // in case things are slow

// eslint-disable-next-line no-undef
describe('Transcript Upload Flow (Selenium)', () => {
  let driver;

  // eslint-disable-next-line no-undef
  beforeAll(async () => {
    const options = new chrome.Options();
    options.addArguments('--headless');
    options.addArguments('--no-sandbox');
    options.addArguments('--disable-dev-shm-usage');

    driver = await new Builder()
      .forBrowser('chrome')
      .setChromeOptions(options)
      .build();

    await driver.get('http://localhost:3000/upload-transcript'); // update route if needed
  });

  // eslint-disable-next-line no-undef
  afterAll(async () => {
    await driver.quit();
  });

  // eslint-disable-next-line no-undef
  test('Uploads a transcript PDF and displays results', async () => {
    // Locate file input
    const fileInput = await driver.findElement(By.css('input[type="file"]'));

    // Upload file (make sure test.pdf exists in root/tests directory)
    const filePath = path.resolve(__dirname, 'test.pdf');
    await fileInput.sendKeys(filePath);

    // Click Submit
    const submitBtn = await driver.findElement(By.xpath("//button[normalize-space()='Submit']"));
    await submitBtn.click();

    // Wait for transcript table to show up
    const table = await driver.wait(
      until.elementLocated(By.css('.transcript-table')),
      10000
    );

    // eslint-disable-next-line no-undef
    expect(await table.isDisplayed()).toBe(true);
  });
});
